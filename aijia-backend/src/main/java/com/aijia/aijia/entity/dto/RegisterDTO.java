package com.aijia.aijia.entity.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String roleKey; // 前端传 "OWNER" 或 "REPAIR_MAN"
}
