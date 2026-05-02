package com.aijia.aijia.mapper;

import com.aijia.aijia.entity.Repair;
import com.aijia.aijia.entity.dto.RepairQueryDTO;
import com.aijia.aijia.entity.vo.RepairVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 报修管理表 Mapper 接口
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface RepairMapper extends BaseMapper<Repair> {
    /**
     * 多表联查：带出楼栋、单元、房号、业主名及原生手机号
     */
    IPage<RepairVO> selectRepairPage(Page<Repair> page, @Param("query") RepairQueryDTO query);

}
