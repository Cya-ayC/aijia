package com.aijia.aijia.mapper;

import com.aijia.aijia.entity.Room;
import com.aijia.aijia.entity.vo.RoomVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 房间表 Mapper 接口
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface RoomMapper extends BaseMapper<Room> {
    /**
     * 三表聯查：分頁查詢房間（帶樓棟名、單元名）
     * @param page 分頁參數
     * @param queryWrapper 查詢條件包裝器
     * @return 分頁結果
     */
    Page<RoomVO> selectRoomPage(
            Page<RoomVO> page,
            @Param(Constants.WRAPPER) Wrapper<RoomVO> queryWrapper
    );

    /**
     * 根据业主ID查询名下所有房产详情
     */
    List<Room> selectRoomVOsByOwnerId(@Param("ownerId") Long ownerId);

}
