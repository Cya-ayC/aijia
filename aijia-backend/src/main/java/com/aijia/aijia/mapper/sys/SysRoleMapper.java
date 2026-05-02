package com.aijia.aijia.mapper.sys;

import com.aijia.aijia.entity.sys.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 根据用户ID查询所属的角色Key列表
     */
    @Select("SELECT r.role_key FROM sys_role r " +
            "LEFT JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} ")
    List<String> getRoleKeysByUserId(Long userId);

}
