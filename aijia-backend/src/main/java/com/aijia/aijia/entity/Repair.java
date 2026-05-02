package com.aijia.aijia.entity;

import com.aijia.aijia.common.enums.RepairStatusEnum;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("bus_repair")
public class Repair {
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 报修房间ID */
    private Long roomId;

    /** 报修人ID */
    private Long ownerId;

    /** 报修人联系电话 */
    private String contactPhone;

    /** 报修内容 */
    private String content;

    /** 图片地址(逗号隔开) */
    private String photos;

    /** 状态 (使用枚举) */
    private RepairStatusEnum status;

    /** 维修人员姓名 */
    private String handlerName;

    /** 逻辑删除 */
    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime finishTime;

    private Long handlerId; // 维修人员ID
}
