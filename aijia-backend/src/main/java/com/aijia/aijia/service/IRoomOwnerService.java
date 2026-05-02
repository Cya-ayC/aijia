package com.aijia.aijia.service;

import com.aijia.aijia.entity.RoomOwner;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 房间与住户关联表 服务类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface IRoomOwnerService extends IService<RoomOwner> {

    public void bindRoomToOwner(Long roomId, Long ownerId);
}
