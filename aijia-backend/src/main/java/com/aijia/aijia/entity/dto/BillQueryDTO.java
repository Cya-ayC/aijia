package com.aijia.aijia.entity.dto;

import com.aijia.aijia.common.enums.PayStatusEnum;
import lombok.Data;

@Data
public class BillQueryDTO {
    private Integer current; // 👈 必须有
    private Integer size;    // 👈 必须有
    private Integer year;
    private Integer month;
    private PayStatusEnum payStatus; // 对应 PayStatusEnum
    private String roomNum;    // 支持模糊搜索房间号
    private Long buildingId;   // 按楼栋筛选
    private String ownerName; // 新增业主名查询
    private Long unitId;      // 新增单元ID查询

    // 🚀 新增字段：用于业主端过滤
    private Long userId;
}
