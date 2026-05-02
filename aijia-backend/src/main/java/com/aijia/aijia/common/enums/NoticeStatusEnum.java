package com.aijia.aijia.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

@Getter
public enum NoticeStatusEnum implements IEnum<Integer> {
    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    RECALLED(2, "已撤回");

    private final int value;
    private final String desc;

    NoticeStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    // --- 核心修复：添加这个静态方法 ---
    public static NoticeStatusEnum decode(Integer value) {
        if (value == null) return null;
        for (NoticeStatusEnum status : NoticeStatusEnum.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}
