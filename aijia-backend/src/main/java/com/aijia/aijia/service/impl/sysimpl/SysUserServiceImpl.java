package com.aijia.aijia.service.impl.sysimpl;

import com.aijia.aijia.common.exception.BusinessException;
import com.aijia.aijia.entity.Owner;
import com.aijia.aijia.entity.dto.RegisterDTO;
import com.aijia.aijia.entity.sys.SysRole;
import com.aijia.aijia.entity.sys.SysUser;
import com.aijia.aijia.entity.sys.SysUserRole;
import com.aijia.aijia.mapper.OwnerMapper;
import com.aijia.aijia.mapper.sys.SysRoleMapper;
import com.aijia.aijia.mapper.sys.SysUserMapper;
import com.aijia.aijia.mapper.sys.SysUserRoleMapper;
import com.aijia.aijia.service.sys.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private OwnerMapper ownerMapper;
    @Autowired
    private PasswordEncoder passwordEncoder; // 用于加密密码
    @Override
    public List<SysUser> getUsersByRoleKey(String roleKey) {
        // 调用你刚才在 XML 里写好的 selectUsersByRoleKey
        return baseMapper.selectUsersByRoleKey(roleKey);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterDTO dto) {
        // 1. 🚀 校验：登录账号（Username）是否已存在
        Long userCount = userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, dto.getUsername()));
        if (userCount > 0) {
            throw new BusinessException("该账号名称已被占用，请修改后重试");
        }

        // 2. 🚀 校验：姓名/昵称（Nickname）是否已存在
        Long nickCount = userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getNickname, dto.getNickname()));
        if (nickCount > 0) {
            throw new BusinessException("该姓名/昵称已存在，请换一个名字或联系管理员");
        }

        // 3. 插入 sys_user 表
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setPhone(dto.getPhone());
        user.setStatus(1); // 启用状态
        userMapper.insert(user);

        // 4. 绑定角色关系
        SysRole role = roleMapper.selectOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleKey, dto.getRoleKey()));
        if (role != null) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoleMapper.insert(userRole);
        }

        // 5. 处理业主逻辑
        if ("OWNER".equals(dto.getRoleKey())) {
            // 尝试根据手机号寻找是否已经存在该业主记录（物业预录入）
            Owner existingOwner = ownerMapper.selectOne(new LambdaQueryWrapper<Owner>()
                    .eq(Owner::getPhone, dto.getPhone()));

            if (existingOwner != null) {
                // 情况 A：业主已存在，直接关联 userId
                existingOwner.setUserId(user.getId());
                // 如果预录入的名字是空的，可以用注册时的昵称补全
                if (existingOwner.getName() == null || "".equals(existingOwner.getName())) {
                    existingOwner.setName(dto.getNickname());
                }
                ownerMapper.updateById(existingOwner);
            } else {
                // 情况 B：完全是新业主，插入新记录
                Owner owner = new Owner();
                owner.setUserId(user.getId());
                owner.setName(dto.getNickname());
                owner.setPhone(dto.getPhone());
                ownerMapper.insert(owner);
            }
        }
    }


}
