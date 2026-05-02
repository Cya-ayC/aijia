package com.aijia.aijia.service.impl;

import com.aijia.aijia.common.utils.JwtUtils;
import com.aijia.aijia.entity.Owner;
import com.aijia.aijia.entity.sys.SysMenu;
import com.aijia.aijia.entity.sys.SysUser;
import com.aijia.aijia.mapper.OwnerMapper;
import com.aijia.aijia.mapper.sys.SysMenuMapper;
import com.aijia.aijia.mapper.sys.SysRoleMapper;
import com.aijia.aijia.service.ILoginService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SysMenuMapper sysMenuMapper; // 注入刚写好的菜单Mapper

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public Map<String, Object> login(String username, String password) {
        // 1. 使用 Security 进行认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // 如果认证失败，Spring Security 会抛出异常，外层全局异常处理会自动捕获
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 2. 认证成功，获取用户信息
        // 注意：这里需要强转为你实现的 SysUser，以便拿到 userId
        SysUser user = (SysUser) authentication.getPrincipal();

        // 3. 生成 Token
        String token = jwtUtils.createToken(username);

        // 4. 🚀 级联查询该用户拥有的菜单
        List<SysMenu> menus = sysMenuMapper.selectMenusByUserId(user.getId());

        // 5. 封装结果发给 Controller
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("token", token);
        resultMap.put("menus", menus);

        // 🚀 新增：把用户信息也发给前端
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("nickname", user.getNickname());
        userMap.put("avatar", user.getAvatar());
        List<String> roles = sysRoleMapper.getRoleKeysByUserId(user.getId());
        // 判断角色并处理业务数据
        if ("admin".equals(user.getUsername())) {
            userMap.put("roleKey", "ADMIN");
        }  else if (roles.contains("REPAIR_MAN")) {
            userMap.put("roleKey", "REPAIR_MAN");
        }else {
            userMap.put("roleKey", "OWNER");

            // 🚀 核心改动：如果是 OWNER，去 bus_owner 表拿手机号
            // 注意：这里需要注入 OwnerMapper
            Owner owner = ownerMapper.selectOne(new LambdaQueryWrapper<Owner>()
                    .eq(Owner::getUserId, user.getId()));

            if (owner != null) {
                // 将业主手机号放入 userMap
                userMap.put("phone", owner.getPhone());
                // 建议：把昵称也同步为业主表里的真实姓名
                userMap.put("nickname", owner.getName());
            }
        }
        resultMap.put("user", userMap);

        return resultMap;
    }
}
