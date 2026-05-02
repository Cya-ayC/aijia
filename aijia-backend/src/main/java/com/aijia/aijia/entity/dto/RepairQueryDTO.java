package com.aijia.aijia.entity.dto;

import com.aijia.aijia.common.enums.RepairStatusEnum;
import lombok.Data;

@Data
public class RepairQueryDTO {
    private String roomNum;
    private RepairStatusEnum status; // 直接用枚举接收
    private String ownerName;

    private Long ownerId;
    private Long handlerId;
}
