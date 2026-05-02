package com.aijia.aijia.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardVO {
    // 统计卡片数据
    private Integer totalRooms;      // 总房间数
    private Integer activeOwners;    // 已入驻业主数
    private BigDecimal monthIncome;  // 本月实收物业费
    private Integer pendingRepairs;  // 待处理报修数
}
