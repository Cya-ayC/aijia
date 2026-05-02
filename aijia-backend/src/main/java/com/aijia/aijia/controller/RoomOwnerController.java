package com.aijia.aijia.controller;

import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.service.IRoomOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 房间与住户关联表 前端控制器
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@RestController
@RequestMapping("/room-owner")
public class RoomOwnerController {

    @Autowired
    private IRoomOwnerService roomOwnerService;
    @PostMapping("/bind")
    public Result<Void> bindRoomToOwner(Long roomId, Long ownerId) {
        roomOwnerService.bindRoomToOwner(roomId, ownerId);
        return Result.success();
    }
}
