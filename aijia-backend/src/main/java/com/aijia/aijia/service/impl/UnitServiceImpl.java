package com.aijia.aijia.service.impl;

import com.aijia.aijia.common.exception.BusinessException;
import com.aijia.aijia.entity.Building;
import com.aijia.aijia.entity.Room;
import com.aijia.aijia.entity.Unit;
import com.aijia.aijia.entity.vo.UnitVO;
import com.aijia.aijia.mapper.BuildingMapper;
import com.aijia.aijia.mapper.UnitMapper;
import com.aijia.aijia.service.IRoomService;
import com.aijia.aijia.service.IUnitService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单元表 服务实现类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements IUnitService {

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private UnitMapper unitMapper;

    // 稍后需要注入 IRoomService 来检查是否有房间，目前先注释掉，等做房间模块再开
     @Autowired
     private IRoomService roomService;


    @Override
    public void deleteUnit(Long id) {
        // 1. 存在性校验
        Unit unit = this.getById(id);
        if (unit == null) {
            throw new BusinessException("单元不存在");
        }

        // 2. 级联校验（防止误删房间）
        Long roomCount = roomService.count(new LambdaQueryWrapper<Room>().eq(Room::getUnitId, id));
        if (roomCount > 0) { throw new BusinessException("该单元下还有房间，无法删除"); }

        // 3. 执行单元的逻辑删除
        if (!this.removeById(id)) {
            throw new BusinessException("单元删除失败");
        }

        // 4. 同步更新楼栋的单元数量 (unit_count - 1)
        // SQL 逻辑：UPDATE bus_building SET unit_count = unit_count - 1 WHERE id = ?
        buildingMapper.update(null, new LambdaUpdateWrapper<Building>()
                .setSql("unit_count = unit_count - 1")
                .eq(Building::getId, unit.getBuildingId())
                .gt(Building::getUnitCount, 0)); // 只有大于0才减，防止变成负数
    }

    @Override
    public Page<UnitVO> findUnitPage(Integer current, Integer size, Long buildingId) {
        // 1. 创建分页对象
        Page<UnitVO> page = new Page<>(current, size);

        // 2. 构建查询条件
        LambdaQueryWrapper<UnitVO> wrapper = new LambdaQueryWrapper<>();
        if (buildingId != null) {
            wrapper.eq(UnitVO::getBuildingId, buildingId);
        }

        // 先按楼栋ID排序，再按单元名排序
        wrapper.orderByAsc(UnitVO::getBuildingId).orderByAsc(UnitVO::getName);

        // 3. 执行 Mapper 中定义的自定义联查 SQL
        return unitMapper.selectUnitPage(page, wrapper);
    }
}
