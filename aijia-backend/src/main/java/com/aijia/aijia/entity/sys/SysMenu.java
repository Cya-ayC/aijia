package com.aijia.aijia.entity.sys; // 请根据你的项目实际包名修改

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单权限表实体类
 * 对应数据库表: sys_menu
 */
@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long parentId;

    private String menuName;

    private String path;

    private String component;

    private String perms;

    private String icon;

    /**
     * 菜单类型 (M:目录, C:菜单, F:按钮)
     */
    private String menuType;

    private Integer sortOrder;

    private LocalDateTime createTime;

}
