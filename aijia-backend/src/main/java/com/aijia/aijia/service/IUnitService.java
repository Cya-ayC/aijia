package com.aijia.aijia.service;

import com.aijia.aijia.entity.Unit;
import com.aijia.aijia.entity.vo.UnitVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 单元表 服务类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface IUnitService extends IService<Unit> {

    void deleteUnit(Long id);
    /**
     * 分页查询单元信息（带楼栋名称）
     */
    Page<UnitVO> findUnitPage(Integer current, Integer size, Long buildingId);
}
