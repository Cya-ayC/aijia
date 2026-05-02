package com.aijia.aijia.common.resultful;

import lombok.Data;

/**
 * 统一结果返回类
 */
@Data
public class Result<T> {
    private Integer code;    // 状态码 (200:成功, 500:失败)
    private String msg;      // 提示消息
    private T data;          // 返回的数据

    // 成功：不带数据
    public static <T> Result<T> success() {
        return success(null);
    }

    // 成功：带数据
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // 失败：自定义错误信息
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
}
