package com.aijia.aijia.mapper.sys;

import com.aijia.aijia.entity.sys.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 根据用户ID查询拥有的菜单列表
     */
    List<SysMenu> selectMenusByUserId(@Param("userId") Long userId);
}
