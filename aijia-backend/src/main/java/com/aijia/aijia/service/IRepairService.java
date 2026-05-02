package com.aijia.aijia.service;

import com.aijia.aijia.entity.Repair;
import com.aijia.aijia.entity.dto.RepairQueryDTO;
import com.aijia.aijia.entity.vo.RepairVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 报修管理表 服务类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface IRepairService extends IService<Repair> {

    /** 分页联查报修单 */
    IPage<RepairVO> selectRepairPage(Page<Repair> page, RepairQueryDTO query);

    void handleRepair(Repair repair);

    void completeRepair(Long id);
}
