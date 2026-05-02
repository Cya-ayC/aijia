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
 * 楼栋信息表
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@Data
@TableName("bus_building")
public class Building implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 楼栋名称 (如: 1号楼)
     */
    private String name;

    /**
     * 单元数量
     */
    private Integer unitCount;

    /**
     * 总层数
     */
    private Integer floorCount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 逻辑删除 (0-正常, 1-已删)
     */
    @TableLogic
    private Byte isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
