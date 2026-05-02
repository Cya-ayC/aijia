package com.aijia.aijia.service.impl;

import com.aijia.aijia.common.enums.RelationType;
import com.aijia.aijia.common.enums.RoomStatus;
import com.aijia.aijia.common.exception.BusinessException;
import com.aijia.aijia.entity.Room;
import com.aijia.aijia.entity.RoomOwner;
import com.aijia.aijia.mapper.RoomOwnerMapper;
import com.aijia.aijia.service.IRoomOwnerService;
import com.aijia.aijia.service.IRoomService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * <p>
 * 房间与住户关联表 服务实现类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@Service
public class RoomOwnerServiceImpl extends ServiceImpl<RoomOwnerMapper, RoomOwner> implements IRoomOwnerService {

    @Autowired
    private IRoomService roomService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindRoomToOwner(Long roomId, Long ownerId) {
        Room room = roomService.getById(roomId);
        if (room == null) {
            throw new BusinessException("该房间不存在");
        }

        // 1. 唯一性校验
        Long count = this.baseMapper.selectCount(new LambdaQueryWrapper<RoomOwner>()
                .eq(RoomOwner::getRoomId, roomId)
                .eq(RoomOwner::getRelationType, RelationType.OWNER));

        if (count > 0) {
            throw new BusinessException("该房间已绑定业主，不可重复绑定");
        }

        // 2. 插入关联关系
        RoomOwner ro = new RoomOwner();
        ro.setRoomId(roomId);
        ro.setOwnerId(ownerId);
        ro.setRelationType(RelationType.OWNER);
        this.save(ro);

        // 3. 联动修改房间状态及【插入交房时间】
        roomService.update(new LambdaUpdateWrapper<Room>()
                .eq(Room::getId, roomId)
                .set(Room::getStatus, RoomStatus.OCCUPIED) // 2-入住
                // 🚀 核心改动：同步设置起收日期为当前日期
                .set(Room::getDeliveryDate, LocalDate.now()));
    }



}
