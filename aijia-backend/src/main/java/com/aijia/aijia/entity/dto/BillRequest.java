package com.aijia.aijia.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 专门用于接收 Postman 参数的 DTO 类
 */
@Data
public class BillRequest {
    // 关键：必须指定格式，否则 Jackson 无法将字符串转为 LocalDate
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetMonth;

    private BigDecimal monthlyPrice;
}