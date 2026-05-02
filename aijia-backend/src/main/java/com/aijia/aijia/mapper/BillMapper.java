package com.aijia.aijia.mapper;

import com.aijia.aijia.entity.Bill;
import com.aijia.aijia.entity.dto.BillQueryDTO;
import com.aijia.aijia.entity.vo.BillVO;
import com.aijia.aijia.entity.vo.FinanceReportVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 物业费账单表 Mapper 接口
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface BillMapper extends BaseMapper<Bill> {

    // IPage 是 MyBatis-Plus 的分页对象
    IPage<BillVO> selectBillPage(Page<Bill> page, @Param("query") BillQueryDTO query);

    FinanceReportVO getFinanceReport(@Param("year") Integer year);
}
