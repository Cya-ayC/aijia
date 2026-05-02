package com.aijia.aijia.controller;

import com.aijia.aijia.common.enums.RepairStatusEnum;
import com.aijia.aijia.common.enums.RoomStatus;
import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.entity.Repair;
import com.aijia.aijia.entity.Room;
import com.aijia.aijia.entity.vo.DashboardVO;
import com.aijia.aijia.entity.vo.FinanceReportVO;
import com.aijia.aijia.service.IBillService;
import com.aijia.aijia.service.IRepairService;
import com.aijia.aijia.service.IRoomService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private IRoomService roomService;
    @Autowired
    private IRepairService repairService;
    @Autowired
    private IBillService billService;

    @GetMapping("/status")
    public Result<DashboardVO> getStats() {
        DashboardVO vo = new DashboardVO();

        // 1. 总房间数
        vo.setTotalRooms((int) roomService.count());

        // 2. 入住业主数 (状态为 OCCUPIED 的房间)
        vo.setActiveOwners((int) roomService.count(new LambdaQueryWrapper<Room>()
                .eq(Room::getStatus, RoomStatus.OCCUPIED)));

        // 3. 本月实收金额 (pay_status = 1 且 pay_time 是本月的)
        // 这里可以根据你现有的 bill 统计逻辑进行简单汇总
        FinanceReportVO report = billService.getFinanceReport(LocalDate.now().getYear());
        vo.setMonthIncome(report.getPaidAmount());

        // 4. 待处理报修数
        vo.setPendingRepairs((int) repairService.count(new LambdaQueryWrapper<Repair>()
                .eq(Repair::getStatus, RepairStatusEnum.PENDING)));

        return Result.success(vo);
    }
}
