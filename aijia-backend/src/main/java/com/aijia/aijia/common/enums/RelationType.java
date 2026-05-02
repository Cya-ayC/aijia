package com.aijia.aijia.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RelationType {
    OWNER(1, "业主"),
    FAMILY(2, "家属"),
    TENANT(3, "租客");

    @EnumValue // 告诉 MyBatis-Plus 数据库存的是这个 code (tinyint)
    private final Integer code;

    @JsonValue // 告诉 Jackson 序列化时返回给前端的是这个描述文字
    private final String desc;

    RelationType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
