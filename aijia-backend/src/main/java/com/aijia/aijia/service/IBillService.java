package com.aijia.aijia.service;

import com.aijia.aijia.entity.Bill;
import com.aijia.aijia.entity.dto.BillQueryDTO;
import com.aijia.aijia.entity.vo.BillVO;
import com.aijia.aijia.entity.vo.FinanceReportVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 物业费账单表 服务类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface IBillService extends IService<Bill> {

    public void generateYearlyBill(Integer year, BigDecimal monthlyPrice, LocalDateTime deadline);

    /**
     * 分页查询账单（联查房间、楼栋、业主）
     */
    IPage<BillVO> selectBillPage(Page<Bill> page, BillQueryDTO query);

    /**
     * 确认缴费（支持批量）
     * @param ids 账单ID列表
     */
    void payBills(List<Long> ids);

    /**
     * 财务概览统计
     */
    FinanceReportVO getFinanceReport(@Param("year") Integer year);


    void generateMonthlyBill(LocalDate targetMonth, BigDecimal monthlyPrice);
}
