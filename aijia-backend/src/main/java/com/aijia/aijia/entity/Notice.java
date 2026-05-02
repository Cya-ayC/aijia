package com.aijia.aijia.entity;

import com.aijia.aijia.common.enums.NoticeStatusEnum;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 社区公告表
 */
@Data
@TableName("bus_notice")
public class Notice {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** 类型 (1:全体公告, 2:楼栋公告, 3:个人催缴) */
    private Integer type;

    /** 状态 (0:草稿, 1:发布, 2:撤回) */
    private NoticeStatusEnum status;

    /** 目标ID (全体:null, 楼栋:BuildingID, 个人:OwnerID) */
    private Long targetId;

    /** 阅读状态 (0:未读, 1:已读) */
    private Integer isRead;

    /** 发布人ID */
    private Long authorId;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
