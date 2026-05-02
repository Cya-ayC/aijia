package com.aijia.aijia.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 业主表实体类
 * 对应数据库表: bus_owner
 */
@Data
@TableName("bus_owner")
public class Owner {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 身份证号 */
    private String idCard;

    /** 性别 (0:男, 1:女) */
    private Integer sex;

    /**
     * 🚀 新增字段：关联系统用户ID
     * 用于建立“登录账号”与“业主信息”的绑定关系
     */
    private Long userId;

    /** 逻辑删除 */
    @TableLogic
    private Integer isDeleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
