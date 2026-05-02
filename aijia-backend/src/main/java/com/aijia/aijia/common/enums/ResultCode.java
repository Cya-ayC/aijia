package com.aijia.aijia.common.enums;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    ERROR(500, "系统异常"),
    BUSINESS_ERROR(400, "业务逻辑错误"),
    UNAUTHORIZED(401, "暂无权限");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
