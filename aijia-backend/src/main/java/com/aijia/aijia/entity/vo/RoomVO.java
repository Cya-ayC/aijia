package com.aijia.aijia.entity.vo;

import com.aijia.aijia.entity.Room;
import lombok.Data;

@Data
public class RoomVO extends Room {
    private String buildingName; // 楼栋名
    private String unitName;     // 单元名
    private String ownerName;    // 业主姓名
    private String ownerPhone;   // 业主电话
}
