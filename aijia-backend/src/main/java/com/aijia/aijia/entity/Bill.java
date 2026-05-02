package com.aijia.aijia.entity;

import com.aijia.aijia.common.enums.PayStatusEnum;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 物业费账单表
 */
@Data
@TableName("bus_bill")
public class Bill {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 房间ID */
    private Long roomId;

    /** 年份 (如: 2024) */
    private Integer year;

    /** 月份 (1-12，年度账单可设为0) */
    private Integer month;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 应缴总额 (面积 * 单价) */
    private BigDecimal totalAmount;

    /** 支付状态 (0:待缴费, 1:已缴费) */
    private PayStatusEnum payStatus;

    /** 缴费备注 */
    private String remark;

    /** 实际缴费时间 */
    private LocalDateTime payTime;

    /** 缴费截止日期 */
    private LocalDateTime deadline;

    /** 逻辑删除 (0-正常, 1-已删) */
    @TableLogic
    private Integer isDeleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Integer type;        // 费用类型 (1:物业费, 2:水费...)后续扩展使用
    private LocalDate billDate;  // 账单月份 (取当月1号)
    private LocalDate startDate; // 计费开始
    private LocalDate endDate;   // 计费结束
}
