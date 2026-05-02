package com.aijia.aijia.entity;

import com.aijia.aijia.common.enums.RelationType;
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
 * 房间与住户关联表
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@Data
@TableName("bus_room_owner")
public class RoomOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 业主ID
     */
    private Long ownerId;

    /**
     * 关系 (1:业主, 2:家属, 3:租客)
     */
    private RelationType relationType;

    private LocalDateTime createTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Byte isDeleted;

    private LocalDateTime updateTime;
}
