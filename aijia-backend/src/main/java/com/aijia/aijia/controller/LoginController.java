package com.aijia.aijia.controller;

import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.entity.dto.LoginDTO;
import com.aijia.aijia.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/bus/user")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> loginData = loginService.login(loginDTO.getUsername(), loginDTO.getPassword());
        // 这里的 loginData 就会被塞进 Result 的 data 字段中
        return Result.success(loginData);
    }

}
