package com.aijia.aijia.controller;

import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.entity.Room;
import com.aijia.aijia.entity.vo.RoomVO;
import com.aijia.aijia.service.IRoomService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 房间表 前端控制器
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private IRoomService roomService;

    /**
     * 分頁查詢房間列表（聯查樓棟和單元名稱）
     */
    @GetMapping("/page")
    public Result<Page<RoomVO>> findPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) Long unitId,
            @RequestParam(required = false) String ownerName) {

        Page<RoomVO> result = roomService.findRoomPage(current, size, buildingId, unitId, ownerName);
        return Result.success(result);
    }

    /**
     * 一鍵初始化房間
     * @param unitId 哪一個單元
     * @param roomsPerFloor 每層樓生幾戶（如：4）
     */
    @PostMapping("/init")
    public Result<Void> initRooms(
            @RequestParam Long unitId,
            @RequestParam Integer roomsPerFloor,// 接收前端传来的每层房间数量
            @RequestParam BigDecimal area) { // 接收前端传来的面积
        roomService.initRooms(unitId, roomsPerFloor, area);
        return Result.success(null);
    }


    /**
     * 修改房間信息（例如修改狀態：閒置 -> 已售）
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Room room) {
        roomService.updateById(room);
        return Result.success(null);
    }

    /**
     * 刪除房間（邏輯刪除）
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return Result.success(null);
    }

    /**
     * 批量导入房间（Excel）
     */
    @PostMapping("/import")
    public Result<Void> importExcel(@RequestParam("file") MultipartFile file) {
        roomService.importRooms(file);
        return Result.success(null);
    }

    @GetMapping("/my")
    public Result<List<Room>> getMyRooms() {
        return Result.success(roomService.getMyRooms());
    }

}
