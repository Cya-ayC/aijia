package com.aijia.aijia.service;

import com.aijia.aijia.entity.Owner;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 业主基础信息表 服务类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface IOwnerService extends IService<Owner> {

     void deleteOwner(Long id);

    public void addOwner(Owner owner);
}
