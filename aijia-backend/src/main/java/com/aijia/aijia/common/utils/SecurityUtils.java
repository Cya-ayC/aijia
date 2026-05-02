package com.aijia.aijia.common.utils;

import com.aijia.aijia.entity.sys.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * 获取当前登录用户信息
     */
    public static SysUser getLoginUser() {
        // 增加一个防御性判断，防止未登录时调用报错
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SysUser) {
            return (SysUser) principal;
        }
        return null;
    }

    /**
     * 获取当前登录用户ID
     */
    public static Long getUserId() {
        SysUser user = getLoginUser();
        return user != null ? user.getId() : null;
    }

    /**
     * 判断是否为管理员
     */
    public static boolean isAdmin() {
        SysUser user = getLoginUser();
        // 这里建议根据你之前数据库里的 roleKey 判断，比如 "admin"
        return user != null && "admin".equals(user.getRoleKey());
    }

    /**
     * 🚀 新增：判断当前登录人是否为“业主”
     */
    public static boolean isOwner() {
        SysUser user = getLoginUser();
        // 根据你之前 localStorage 存的 roleKey 为 "OWNER" 来判断
        return user != null && "OWNER".equals(user.getRoleKey());
    }

    public static boolean isRepairMan() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return false;

        // 检查权限列表中是否包含 "ROLE_REPAIR_MAN"
        // 注意：Spring Security 默认会给角色加 ROLE_ 前缀
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("REPAIR_MAN")
                        || a.getAuthority().equals("ROLE_REPAIR_MAN"));
    }


}
