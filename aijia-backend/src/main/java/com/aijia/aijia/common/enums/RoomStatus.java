package com.aijia.aijia.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RoomStatus {
    VACANT(0, "闲置"),
    SOLD(1, "已售"),
    OCCUPIED(2, "入住");

    @EnumValue // 告诉 MyBatis-Plus 数据库存的是这个 code
    private final Integer code;
    @JsonValue // 核心：加上这个，前端收到的 status 字段就会直接是 "闲置"
    private final String desc;

    RoomStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    // 关键点：增加一个反序列化方法
    @JsonCreator
    public static RoomStatus decode(Object value) {
        if (value == null) return null;
        for (RoomStatus status : RoomStatus.values()) {
            // 只要 code、name(OCCUPIED) 或 desc(入住) 匹配上任何一个就行
            if (status.name().equalsIgnoreCase(value.toString()) ||
                    status.desc.equals(value.toString()) ||
                    status.code.toString().equals(value.toString())) {
                return status;
            }
        }
        return null;
    }
}
