package com.aijia.aijia.service.impl;

import com.aijia.aijia.common.exception.BusinessException;
import com.aijia.aijia.entity.Building;
import com.aijia.aijia.entity.Room;
import com.aijia.aijia.entity.Unit;
import com.aijia.aijia.entity.dto.BuildingImportDTO;
import com.aijia.aijia.mapper.BuildingMapper;
import com.aijia.aijia.service.IBuildingService;
import com.aijia.aijia.service.IRoomService;
import com.aijia.aijia.service.IUnitService;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements IBuildingService {

    @Autowired
    private IUnitService unitService;

    @Override
    public void addBuilding(Building building) {
        // 1. 非空校验
        if (StringUtils.isBlank(building.getName())) {
            throw new BusinessException("楼栋名称不能为空");
        }

        // 2. 重复校验
        Long count = this.baseMapper.selectCount(new LambdaQueryWrapper<Building>()
                .eq(Building::getName, building.getName()));

        if (count > 0) {
            throw new BusinessException("楼栋名称 [" + building.getName() + "] 已存在");
        }

        // 3. 执行保存
        if (!this.save(building)) {
            throw new BusinessException("添加楼栋失败，数据库操作异常");
        }

        // 3. 自动生成单元逻辑
        Integer unitCount = building.getUnitCount();
        if (unitCount != null && unitCount > 0) {
            List<Unit> unitList = new ArrayList<>();
            for (int i = 1; i <= unitCount; i++) {
                Unit unit = new Unit();
                unit.setBuildingId(building.getId()); // 获取刚保存成功的楼栋ID
                unit.setName(i + "单元");            // 固定格式：x单元
                unitList.add(unit);
            }
            // 批量保存单元
            unitService.saveBatch(unitList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 必须开启事务
    public void deleteBuilding(Long id) {
        // 1. 存在性校验
        Building building = this.getById(id);
        if (building == null) {
            throw new BusinessException("该楼栋不存在或已被删除");
        }

        // 2. 检查该楼栋下是否有房间 (这是最高级别的校验，防止误删已入住数据)
        // TODO: 等房间模块 IRoomService 准备好后开启
    /*
    Long roomCount = roomService.count(new LambdaQueryWrapper<Room>().eq(Room::getBuildingId, id));
    if (roomCount > 0) {
        throw new BusinessException("该楼栋下已有房间数据，无法删除");
    }
    */

        // 3. 执行级联删除：删除该楼栋关联的所有单元
        // 直接构造条件：UPDATE bus_unit SET is_deleted = 1 WHERE building_id = #{id}
        LambdaUpdateWrapper<Unit> unitUpdateWrapper = new LambdaUpdateWrapper<>();
        unitUpdateWrapper.eq(Unit::getBuildingId, id)
                .set(Unit::getIsDeleted, 1); // 手动设置逻辑删除位
        unitService.update(unitUpdateWrapper);

        // 4. 执行楼栋本身的逻辑删除
        if (!this.removeById(id)) {
            throw new BusinessException("楼栋删除失败");
        }
    }


    @Autowired
    private IRoomService roomService; // 注入房间服务

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBuilding(Building building) {
        // 1. 存在性校验
        Building oldBuilding = this.getById(building.getId());
        if (oldBuilding == null) {
            throw new BusinessException("该楼栋不存在");
        }

        // --- 核心校验：检查该楼栋下是否已经有了房间数据 ---
        // 只要有房间，就说明已经进入正式管理阶段，不能随便动物理结构了
        Long roomCount = roomService.count(new LambdaQueryWrapper<Room>()
                .eq(Room::getBuildingId, building.getId()));
        boolean hasRooms = roomCount > 0;

        // 2. 单元数量变化校验
        Integer oldUnitNum = oldBuilding.getUnitCount();
        Integer newUnitNum = building.getUnitCount();
        if (newUnitNum != null && !newUnitNum.equals(oldUnitNum)) {
            if (newUnitNum > oldUnitNum) {
                // 增加单元：自动补齐逻辑保持不变
                List<Unit> newUnits = new ArrayList<>();
                for (int i = oldUnitNum + 1; i <= newUnitNum; i++) {
                    Unit unit = new Unit();
                    unit.setBuildingId(building.getId());
                    unit.setName(i + "单元");
                    newUnits.add(unit);
                }
                unitService.saveBatch(newUnits);
            } else {
                // 减少单元：如果有房间，绝对不能减
                if (hasRooms) {
                    throw new BusinessException("该楼栋下已有房间数据，禁止减少单元数量以防数据丢失");
                }
            }
        }

        // 3. 楼层数量变化校验
        if (!oldBuilding.getFloorCount().equals(building.getFloorCount())) {
            if (hasRooms) {
                throw new BusinessException("该楼栋下已有房间数据，禁止修改总层数");
            }
        }

        // 4. 重名校验（排除自己）
        if (StringUtils.isNotBlank(building.getName())) {
            Long count = this.baseMapper.selectCount(new LambdaQueryWrapper<Building>()
                    .eq(Building::getName, building.getName())
                    .ne(Building::getId, building.getId()));
            if (count > 0) {
                throw new BusinessException("楼栋名称 [" + building.getName() + "] 已存在");
            }
        }

        // 5. 执行更新
        if (!this.updateById(building)) {
            throw new BusinessException("修改失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 开启事务
    public void importBuildings(MultipartFile file) {
        try {
            // 使用 EasyExcel 读取文件
            List<BuildingImportDTO> list = EasyExcel.read(file.getInputStream())
                    .head(BuildingImportDTO.class)
                    .sheet()
                    .doReadSync();

            if (list == null || list.isEmpty()) {
                throw new BusinessException("导入数据不能为空");
            }

            // 转换并校验
            for (BuildingImportDTO dto : list) {
                Building building = new Building();
                // 这里可以利用 BeanUtils 拷贝属性，也可以手动 set
                building.setName(dto.getName());
                building.setUnitCount(dto.getUnitCount());
                building.setFloorCount(dto.getFloorCount());
                building.setRemark(dto.getRemark());

                // 调用你之前写好的 addBuilding 方法，它会自动校验重名
                this.addBuilding(building);
            }
        } catch (IOException e) {
            throw new BusinessException("读取 Excel 文件失败");
        }
    }


}
