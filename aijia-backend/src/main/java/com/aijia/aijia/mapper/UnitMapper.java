package com.aijia.aijia.mapper;

import com.aijia.aijia.entity.Unit;
import com.aijia.aijia.entity.vo.UnitVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 单元表 Mapper 接口
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface UnitMapper extends BaseMapper<Unit> {




        Page<UnitVO> selectUnitPage(Page<UnitVO> page, @Param("ew") Wrapper queryWrapper);


}
