package com.aijia.aijia.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 系统用户实体类
 * 实现了 Spring Security 的 UserDetails 接口
 */
@Data
@TableName("sys_user")
public class SysUser implements UserDetails {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 昵称 */
    private String nickname;

    /** 头像地址 */
    private String avatar;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 状态 (1:正常, 0:禁用) */
    private Integer status;

    /** 逻辑删除 (0:未删除, 1:已删除) */
    @TableLogic
    private Integer isDeleted;

    /** 角色标识 (如: admin, OWNER) */
    @TableField(exist = false)
    private String roleKey;

    /** 创建时间 (由 MyBatis-Plus 自动填充) */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 (由 MyBatis-Plus 自动填充) */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // --- 权限/安全 核心方法 ---

    /**
     * 存储当前用户的权限列表
     * @TableField(exist = false) 表示该字段不对应数据库表的任何列
     */
    @TableField(exist = false)
    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 如果状态为 1 则不锁定，否则锁定
        return status != null && status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 账号是否启用
        return status != null && status == 1;
    }
}
