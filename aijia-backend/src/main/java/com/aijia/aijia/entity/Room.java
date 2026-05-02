package com.aijia.aijia.entity;

import com.aijia.aijia.common.enums.RoomStatus;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 房间表
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@Data
@TableName("bus_room")
public class Room implements Serializable {

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

    private String buildingName;

    /**
     * 所属单元ID
     */
    private Long unitId;
    private String unitName;
    /**
     * 房间号 (如: 101)
     */
    private String roomNum;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 建筑面积
     */
    private BigDecimal area;

    /**
     * 状态 (0:闲置, 1:已售, 2:入住)
     */
    private RoomStatus status;
    private LocalDate deliveryDate; // 交房/起收日期

    /**
     * 房屋类型(1:住宅, 2:商铺, 3:其他)
     */
    private Byte roomType;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Byte isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;



}
