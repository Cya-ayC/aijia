package com.aijia.aijia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 单元表
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@Data
@TableName("bus_unit")
public class Unit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属楼栋ID
     */
    private Long buildingId;

    /**
     * 单元名称 (如: 1单元)
     */
    private String name;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Byte isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
