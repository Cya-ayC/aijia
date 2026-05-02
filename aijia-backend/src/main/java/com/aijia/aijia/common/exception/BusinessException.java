package com.aijia.aijia.common.exception;

/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException {
    private Integer code; // 错误状态码

    public BusinessException(String message) {
        super(message);
        this.code = 500; // 默认业务错误码
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
