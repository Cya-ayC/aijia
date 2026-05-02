package com.aijia.aijia.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        // 打印出来的就是 123456 加密后的样子
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
