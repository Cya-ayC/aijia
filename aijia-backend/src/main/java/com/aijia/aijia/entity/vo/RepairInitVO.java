package com.aijia.aijia.entity.vo;

import com.aijia.aijia.entity.Room;
import lombok.Data;

import java.util.List;

@Data
public class RepairInitVO {
    private String ownerName;    // 业主姓名
    private String contactPhone; // 默认联系电话
    private List<Room> myRooms;  // 直接使用 Room 实体类列表
}
