package com.aijia.aijia.common.exception.handler;

import com.aijia.aijia.common.exception.BusinessException;
import com.aijia.aijia.common.resultful.Result;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常 BusinessException
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        // 返回你统一的 Result 格式，设置错误信息
        return Result.error(e.getMessage());
    }

    /**
     * 处理系统未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        e.printStackTrace(); // 打印错误日志供后端排查
        return Result.error("系统发生异常，请联系管理员");
    }

    // 🚀 新增：专门处理 Spring Security 认证失败的异常
    @ExceptionHandler(BadCredentialsException.class)
    public Result handleBadCredentialsException(BadCredentialsException e) {
        // 当用户名或密码不正确时，会触发这个异常
        return Result.error("用户名或密码错误");
    }
}
