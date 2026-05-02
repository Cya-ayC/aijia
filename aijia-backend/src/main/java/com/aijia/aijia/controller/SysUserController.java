package com.aijia.aijia.controller;

import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.entity.dto.RegisterDTO;
import com.aijia.aijia.entity.sys.SysUser;
import com.aijia.aijia.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    /**
     * 根据角色标识获取用户列表
     * GET /sys/user/listByRole/REPAIR_MAN
     */
    @GetMapping("/listByRole/{roleKey}")
    public Result<List<SysUser>> listByRole(@PathVariable String roleKey) {
        List<SysUser> users = userService.getUsersByRoleKey(roleKey);
        return Result.success(users);
    }

    /**
     * 用户注册接口
     * 包含：账号创建、角色分配、业主身份关联（若为业主）
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDTO registerDTO) {
        // 1. 简单的参数校验（也可以在 DTO 上使用 @NotBlank 等注解）
        if (registerDTO.getUsername() == null || registerDTO.getUsername().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (registerDTO.getPassword() == null || registerDTO.getPassword().length() < 6) {
            return Result.error("密码长度不能少于6位");
        }
        if (registerDTO.getRoleKey() == null) {
            return Result.error("请选择注册身份");
        }

        // 2. 调用 Service 层的一站式注册逻辑
        userService.register(registerDTO);

        return Result.success();
    }
}
