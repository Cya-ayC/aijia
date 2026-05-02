package com.aijia.aijia.service.impl.sysimpl;

import com.aijia.aijia.entity.sys.SysUser;
import com.aijia.aijia.mapper.sys.SysMenuMapper;
import com.aijia.aijia.mapper.sys.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 当 Security 尝试登录时，会自动调用这个方法
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 去数据库查人
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));

        // 2. 没查到直接报错
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码不正确");
        }

        // 3. 将来我们会在这里查询用户的权限标识(perms)，现在先传一个空列表保证登录逻辑跑通
        user.setAuthorities(new ArrayList<>());

        // 4. 返回这个 SysUser。
        // 因为你的 SysUser 已经实现了 UserDetails 接口，所以可以直接返回
        return user;
    }
}
