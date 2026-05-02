package com.aijia.aijia.service.impl;

import com.aijia.aijia.common.enums.RepairStatusEnum;
import com.aijia.aijia.common.exception.BusinessException;
import com.aijia.aijia.common.utils.SecurityUtils;
import com.aijia.aijia.entity.Repair;
import com.aijia.aijia.entity.dto.RepairQueryDTO;
import com.aijia.aijia.entity.vo.RepairVO;
import com.aijia.aijia.mapper.RepairMapper;
import com.aijia.aijia.service.IRepairService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 报修管理表 服务实现类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements IRepairService {

    @Override
    public IPage<RepairVO> selectRepairPage(Page<Repair> page, RepairQueryDTO query) {
        if (SecurityUtils.isOwner()) {
            query.setOwnerId(SecurityUtils.getUserId());
        } else if (SecurityUtils.isRepairMan()) {
            // 🚀 核心改动：如果是维修工登录，只查指派给他的单子
            query.setHandlerId(SecurityUtils.getUserId());
        }
        return baseMapper.selectRepairPage(page, query);
    }


    // 在 RepairServiceImpl.java 中
    @Override
    public void handleRepair(Repair repair) {
        // 如果传入了 handlerId，说明正在派单
        if (repair.getHandlerId() != null) {
            // 🚀 自动将状态改为“处理中”，并记录派单动作
            repair.setStatus(RepairStatusEnum.HANDLING);
        }
        this.updateById(repair);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeRepair(Long id) {
        // 1. 先检查报修单是否存在
        Repair repair = this.getById(id);
        if (repair == null) {
            throw new BusinessException("报修单不存在");
        }

        Repair oldRepair = this.getById(id);
        // 权限校验：如果是师傅操作，必须是该单子的负责人
        if (SecurityUtils.isRepairMan() && !oldRepair.getHandlerId().equals(SecurityUtils.getUserId())) {
            throw new BusinessException("您不是该单据的负责人，无法操作");
        }

        // 2. 更新状态和完成时间
        LambdaUpdateWrapper<Repair> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Repair::getId, id)
                .set(Repair::getStatus, RepairStatusEnum.COMPLETED) // 假设你用的是字符串或枚举
                .set(Repair::getFinishTime, LocalDateTime.now()); // 🚀 记录当前时间为完成时间

        this.update(updateWrapper);
    }



}
