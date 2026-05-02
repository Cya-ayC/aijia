package com.aijia.aijia.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class SysRole {
    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称 (如：超级管理员、维修员)
     */
    private String roleName;

    /**
     * 角色权限字符串 (如：ADMIN, REPAIR_MAN, OWNER)
     */
    private String roleKey;

    /**
     * 显示顺序
     */
    private Integer sortOrder;

    /**
     * 角色状态 (0正常 1停用)
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT) // 自动填充
    private LocalDateTime createTime;

}
