package com.aijia.aijia.controller;

import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.entity.Bill;
import com.aijia.aijia.entity.dto.BillGenerateDTO;
import com.aijia.aijia.entity.dto.BillQueryDTO;
import com.aijia.aijia.entity.dto.BillRequest;
import com.aijia.aijia.entity.vo.FinanceReportVO;
import com.aijia.aijia.service.IBillService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 物业费账单表 前端控制器
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private IBillService billService;

    // 分页查询账单
    // BillController.java
    @GetMapping("/page")
    public Result getPage(BillQueryDTO query) {
        // 🚀 直接从 query 对象里取 current 和 size
        // 确保你的 BillQueryDTO 类里有 Integer current 和 Integer size 字段
        Page<Bill> page = new Page<>(query.getCurrent(), query.getSize());

        return Result.success(billService.selectBillPage(page, query));
    }


    // 批量生成账单(年）
    @PostMapping("/generate")
    public Result<String> generate(@RequestBody BillGenerateDTO dto) {
        billService.generateYearlyBill(dto.getYear(), dto.getMonthlyPrice(), dto.getDeadline());
        return Result.success("账单生成任务已提交");
    }
    /**
     * 生成月度账单接口
     */
    @PostMapping("/generate/month")
    public String generate(@RequestBody BillRequest request) {
        billService.generateMonthlyBill(request.getTargetMonth(), request.getMonthlyPrice());
        return "success";
    }

    /**
     * 确认缴费
     * Body 传参: [1, 2, 3]
     */
    @PutMapping("/pay")
    public Result<String> pay(@RequestBody List<Long> ids) {
        billService.payBills(ids);
        return Result.success("缴费登记成功");
    }


    /**
     * 财务概览统计
     * GET /bill/report?year=2024
     */
    @GetMapping("/report")
    public Result<FinanceReportVO> getReport(@RequestParam(required = false) Integer year) {
        return Result.success(billService.getFinanceReport(year));
    }

}
