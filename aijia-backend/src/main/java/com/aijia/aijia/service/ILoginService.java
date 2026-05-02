package com.aijia.aijia.service;

import java.util.Map;

public interface ILoginService {

    /**
     * 登录并返回 Token 和 菜单
     */
    Map<String, Object> login(String username, String password);
}
