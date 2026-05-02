package com.aijia.aijia.service;

import com.aijia.aijia.entity.Room;
import com.aijia.aijia.entity.vo.RoomVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 房间表 服务类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface IRoomService extends IService<Room> {

    void initRooms(Long unitId, Integer roomsPerFloor, BigDecimal area);


    // 新增分页查询声明
    Page<RoomVO> findRoomPage(Integer current, Integer size, Long buildingId, Long unitId, String ownerName);

    void importRooms(MultipartFile file);

    boolean deleteRoom(Long id);

    List<Room> getMyRooms();
}
