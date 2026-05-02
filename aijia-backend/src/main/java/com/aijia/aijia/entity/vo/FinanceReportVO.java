package com.aijia.aijia.entity.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FinanceReportVO {
    private BigDecimal totalAmount;   // 应收总额
    private BigDecimal paidAmount;    // 已收实额
    private BigDecimal unpaidAmount;  // 欠费总额
    private Double payRate;           // 收缴率 (已收/应收)
    private Integer totalBillCount;   // 总账单数
    private Integer paidBillCount;    // 已缴账单数
}
