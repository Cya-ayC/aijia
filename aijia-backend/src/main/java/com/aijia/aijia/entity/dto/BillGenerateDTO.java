package com.aijia.aijia.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BillGenerateDTO {
    private Integer year;
    private BigDecimal monthlyPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 接收前端时间字符串
    private LocalDateTime deadline;
}
