package com.aijia.aijia.service.impl;

import com.aijia.aijia.common.enums.RoomStatus;
import com.aijia.aijia.common.exception.BusinessException;
import com.aijia.aijia.common.utils.SecurityUtils;
import com.aijia.aijia.entity.Building;
import com.aijia.aijia.entity.Room;
import com.aijia.aijia.entity.Unit;
import com.aijia.aijia.entity.dto.RoomImportDTO;
import com.aijia.aijia.entity.sys.SysUser;
import com.aijia.aijia.entity.vo.RoomVO;
import com.aijia.aijia.mapper.BuildingMapper;
import com.aijia.aijia.mapper.RoomMapper;
import com.aijia.aijia.mapper.UnitMapper;
import com.aijia.aijia.service.IBuildingService;
import com.aijia.aijia.service.IRoomService;
import com.aijia.aijia.service.IUnitService;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 房间表 服务实现类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {

    @Autowired
    @Lazy
    private IUnitService unitService;

    @Autowired
    private IBuildingService buildingService;

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private UnitMapper unitMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initRooms(Long unitId, Integer roomsPerFloor, BigDecimal area) {
        // 1. 基本校验
        if (area == null || area.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("初始面积必须大于0");
        }

        // 2. 查找单元信息
        Unit unit = unitService.getById(unitId);
        if (unit == null) throw new BusinessException("单元不存在");

        // 3. 安全校验：防止重复生成（如果该单元已有房间，不允许再次初始化）
        Long count = this.baseMapper.selectCount(new LambdaQueryWrapper<Room>()
                .eq(Room::getUnitId, unitId));
        if (count > 0) {
            throw new BusinessException("该单元已初始化过房间，请勿重复操作");
        }

        // 4. 查找所属楼栋获取总层数
        Building building = buildingService.getById(unit.getBuildingId());
        Integer totalFloors = building.getFloorCount();

        // 5. 循环生成房间
        List<Room> roomList = new ArrayList<>();
        for (int f = 1; f <= totalFloors; f++) {
            for (int i = 1; i <= roomsPerFloor; i++) {
                Room room = new Room();
                room.setUnitId(unitId);
                room.setBuildingId(building.getId());
                room.setFloor(f);
                room.setArea(area); // *** 设置初始面积 ***

                // 生成房号逻辑
                String roomNum = f + (i < 10 ? "0" + i : String.valueOf(i));
                room.setRoomNum(roomNum);
                room.setStatus(RoomStatus.VACANT);

                roomList.add(room);
            }
        }
        // 6. 批量插入数据库
        this.saveBatch(roomList);
    }


    @Override
    public Page<RoomVO> findRoomPage(Integer current, Integer size, Long buildingId, Long unitId, String ownerName) {
        // 1. 准备分页对象
        Page<RoomVO> page = new Page<>(current, size);

        // 2. 构造查询条件
        QueryWrapper<RoomVO> wrapper = new QueryWrapper<>();
        wrapper.eq("r.is_deleted", 0); // 房间未删除

        // 楼栋过滤
        if (buildingId != null) {
            wrapper.eq("r.building_id", buildingId);
        }
        // 单元过滤
        if (unitId != null) {
            wrapper.eq("r.unit_id", unitId);
        }

        // --- 新增：按业主姓名模糊查询 ---
        // 注意：这里的 "o.name" 必须和你 RoomMapper.xml 里的 LEFT JOIN 业主表的别名一致
        if (StringUtils.isNotBlank(ownerName)) {
            wrapper.like("o.name", ownerName);
        }

        // 3. 排序
        wrapper.orderByAsc("r.building_id")
                .orderByAsc("r.unit_id")
                .orderByAsc("r.floor")
                .orderByAsc("r.room_num");

        // 4. 调用 Mapper
        return this.baseMapper.selectRoomPage(page, wrapper);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importRooms(MultipartFile file) {
        try {
            List<RoomImportDTO> list = EasyExcel.read(file.getInputStream())
                    .head(RoomImportDTO.class)
                    .sheet()
                    .doReadSync();

            for (RoomImportDTO dto : list) {
                // --- 1. 查找楼栋 (通过名称查对象) ---
                Building building = buildingMapper.selectOne(new LambdaQueryWrapper<Building>()
                        .eq(Building::getName, dto.getBuildingName()));
                if (building == null) {
                    throw new BusinessException("导入失败：找不到楼栋 [" + dto.getBuildingName() + "]");
                }

                // --- 2. 查找单元 (通过名称和楼栋ID查对象) ---
                Unit unit = unitMapper.selectOne(new LambdaQueryWrapper<Unit>()
                        .eq(Unit::getBuildingId, building.getId())
                        .eq(Unit::getName, dto.getUnitName()));
                if (unit == null) {
                    throw new BusinessException("导入失败：[" + dto.getBuildingName() + "] 下找不到单元 [" + dto.getUnitName() + "]");
                }

                // 2. 封装房间对象
                Room room = new Room();
                room.setBuildingId(building.getId());
                room.setUnitId(unit.getId());
                room.setRoomNum(dto.getRoomNum());
                room.setArea(dto.getArea());
                room.setFloor(dto.getFloor());

                // 3. 解析状态：将中文转换为枚举对象
                if (StringUtils.isBlank(dto.getStatusName())) {
                    room.setStatus(RoomStatus.VACANT); // 默认闲置
                } else {
                    // 根据中文匹配枚举
                    RoomStatus status = null;
                    for (RoomStatus rs : RoomStatus.values()) {
                        if (rs.getDesc().equals(dto.getStatusName())) {
                            status = rs;
                            break;
                        }
                    }
                    if (status == null) {
                        throw new BusinessException("房号 " + dto.getRoomNum() + " 的状态 [" + dto.getStatusName() + "] 格式不正确");
                    }
                    room.setStatus(status);
                }

                this.save(room);
            }
        } catch (IOException e) {
            throw new BusinessException("Excel读取失败");
        }
    }

    @Override
    public boolean deleteRoom(Long id) {
        // 1. 先查出该房间的信息
        Room room = this.getById(id);
        if (room == null) {
            throw new BusinessException("该房间不存在");
        }

        // 2. 状态校验：只有 VACANT (闲置) 状态才允许删除
        // 注意：RoomStatus.VACANT 是你之前定义的枚举
        if (room.getStatus() != RoomStatus.VACANT) {
            throw new BusinessException("该房间当前状态为 [" + room.getStatus().getDesc() + "]，禁止删除！");
        }

        // 3. 执行逻辑删除
        return this.removeById(id);
    }

    // IBillService 或 IRoomService 中
    @Override
    public List<Room> getMyRooms() {
        // 1. 从 Security 上下文中获取当前登录用户的 ID
        // 注意：这里的 SysUser 应该包含你存入数据库时的业主 ID
        SysUser user = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. 调用 Mapper 查询
        return baseMapper.selectRoomVOsByOwnerId(user.getId());
    }


}
