package com.aijia.aijia.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

@Getter
public enum RepairStatusEnum implements IEnum<Integer> {
    PENDING(0, "待处理"),
    HANDLING(1, "处理中"),
    COMPLETED(2, "已完成"),
    EVALUATED(3, "已评价");

    private final int value;
    private final String desc;

    RepairStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
