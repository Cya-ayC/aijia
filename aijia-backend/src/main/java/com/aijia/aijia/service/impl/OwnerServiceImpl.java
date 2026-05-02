package com.aijia.aijia.service.impl;

import com.aijia.aijia.common.exception.BusinessException;
import com.aijia.aijia.entity.Owner;
import com.aijia.aijia.entity.RoomOwner;
import com.aijia.aijia.mapper.OwnerMapper;
import com.aijia.aijia.service.IOwnerService;
import com.aijia.aijia.service.IRoomOwnerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 业主基础信息表 服务实现类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@Service
public class OwnerServiceImpl extends ServiceImpl<OwnerMapper, Owner> implements IOwnerService {

    @Autowired
    private IRoomOwnerService roomOwnerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOwner(Long id) {
        // 1. 检查该业主是否存在
        Owner owner = this.getById(id);
        if (owner == null) {
            throw new BusinessException("该业主不存在");
        }

        // 2. 核心校验：检查 bus_room_owner 表中是否还有该业主的关联记录
        Long count = roomOwnerService.count(new LambdaQueryWrapper<RoomOwner>()
                .eq(RoomOwner::getOwnerId, id));

        if (count > 0) {
            throw new BusinessException("该业主名下还有绑定的房产，请先办理【迁出/退房】后再删除业主信息！");
        }

        // 3. 执行逻辑删除
        this.removeById(id);
    }

    @Override
    public void addOwner(Owner owner) {
        // 1. 校验手机号
        if (StringUtils.isNotBlank(owner.getPhone())) {
            Long phoneCount = this.baseMapper.selectCount(new LambdaQueryWrapper<Owner>()
                    .eq(Owner::getPhone, owner.getPhone()));
            if (phoneCount > 0) {
                throw new BusinessException("手机号 [" + owner.getPhone() + "] 已被登记，请勿重复添加");
            }
        }

        // 2. 校验身份证号
        if (StringUtils.isNotBlank(owner.getIdCard())) {
            Long idCardCount = this.baseMapper.selectCount(new LambdaQueryWrapper<Owner>()
                    .eq(Owner::getIdCard, owner.getIdCard()));
            if (idCardCount > 0) {
                throw new BusinessException("身份证号 [" + owner.getIdCard() + "] 已存在，请核对信息");
            }
        }

        // 3. 执行保存
        this.save(owner);
    }

}
