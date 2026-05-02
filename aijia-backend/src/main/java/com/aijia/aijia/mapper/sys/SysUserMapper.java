package com.aijia.aijia.mapper.sys;


import com.aijia.aijia.entity.sys.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据角色标识查询用户列表
     */
    List<SysUser> selectUsersByRoleKey(@Param("roleKey") String roleKey);
}
