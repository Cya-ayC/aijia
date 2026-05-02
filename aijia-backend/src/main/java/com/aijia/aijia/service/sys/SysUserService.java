package com.aijia.aijia.service.sys;

import com.aijia.aijia.entity.dto.RegisterDTO;
import com.aijia.aijia.entity.sys.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    public List<SysUser> getUsersByRoleKey(String roleKey);
    void register(RegisterDTO dto);
}
