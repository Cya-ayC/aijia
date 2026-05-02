package com.aijia.aijia.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

@Getter
public enum PayStatusEnum implements IEnum<Integer> {
    UNPAID(0, "待缴费"),
    PAID(1, "已缴费");

    private final int value;
    private final String desc;

    PayStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
