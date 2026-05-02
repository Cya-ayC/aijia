package com.aijia.aijia.service.impl;

import com.aijia.aijia.common.enums.PayStatusEnum;
import com.aijia.aijia.common.enums.RoomStatus;
import com.aijia.aijia.common.exception.BusinessException;
import com.aijia.aijia.common.utils.SecurityUtils;
import com.aijia.aijia.entity.Bill;
import com.aijia.aijia.entity.Room;
import com.aijia.aijia.entity.dto.BillQueryDTO;
import com.aijia.aijia.entity.sys.SysUser;
import com.aijia.aijia.entity.vo.BillVO;
import com.aijia.aijia.entity.vo.FinanceReportVO;
import com.aijia.aijia.mapper.BillMapper;
import com.aijia.aijia.mapper.RoomMapper;
import com.aijia.aijia.service.IBillService;
import com.aijia.aijia.service.INoticeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 物业费账单表 服务实现类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private INoticeService noticeService; // 注入公告服务
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateYearlyBill(Integer year, BigDecimal monthlyPrice, LocalDateTime deadline) {
        // 校验单价是否合法
        if (monthlyPrice == null || monthlyPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("单价必须大于0");
        }
        // 1. 获取所有需要交费的房间（已售、入住）
        List<Room> rooms = roomMapper.selectList(new LambdaQueryWrapper<Room>()
                .in(Room::getStatus,  RoomStatus.SOLD, RoomStatus.OCCUPIED));

        List<Bill> allBills = new ArrayList<>();

        for (Room room : rooms) {
            for (int m = 1; m <= 12; m++) {
                // 2. 幂等检查：防止重复生成某月账单
                boolean exists = this.count(new LambdaQueryWrapper<Bill>()
                        .eq(Bill::getRoomId, room.getId())
                        .eq(Bill::getYear, year)
                        .eq(Bill::getMonth, m)) > 0;

                if (exists) continue;

                // 3. 构建单月账单
                Bill bill = new Bill();
                bill.setRoomId(room.getId());
                bill.setYear(year);
                bill.setMonth(m);
                bill.setUnitPrice(monthlyPrice);
                // 金额 = 面积 * 月单价
                bill.setTotalAmount(room.getArea().multiply(monthlyPrice).setScale(2, RoundingMode.HALF_UP));//保留两位小鼠
                bill.setPayStatus(PayStatusEnum.UNPAID);
                bill.setDeadline(deadline); // 缴费截止日期
                allBills.add(bill);
            }
        }

        if (!allBills.isEmpty()) {
            this.saveBatch(allBills);
            // 发布一条系统公告
            this.createPaymentNotice(year);
        }
    }


    private void createPaymentNotice(Integer year) {
        String title = year + "年度物业费缴纳提醒";
        String content = "亲爱的业主，" + year + "年度物业费账单已生成，请及时在系统中查询并缴费。";

        // 调用公告模块的发布方法
        noticeService.publishSystemNotice(title, content);
    }

    @Override
    public IPage<BillVO> selectBillPage(Page<Bill> page, BillQueryDTO query) {
        // 获取当前登录用户
        SysUser user = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 🚀 核心：如果是业主（非admin），强制只查他自己的账单
        // 这里的判断逻辑可以根据你的角色体系（比如 user.getRoles() 包含 OWNER）
        if (!"admin".equals(user.getUsername())) {
            query.setUserId(user.getId());
        }

        // 调用原本的 Mapper 分页查询
        return baseMapper.selectBillPage(page, query);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payBills(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要缴费的账单");
        }

        // 1. 查询出这些账单
        List<Bill> bills = this.listByIds(ids);
        if (bills.isEmpty()) {
            throw new BusinessException("未找到相关账单数据");
        }

        // 2. 过滤掉已经交过钱的，防止重复缴费逻辑触发
        List<Bill> pendingBills = bills.stream()
                .filter(b -> b.getPayStatus() == PayStatusEnum.UNPAID)
                .collect(Collectors.toList());

        if (pendingBills.isEmpty()) {
            throw new BusinessException("所选账单已全部缴费，无需重复操作");
        }

        // 3. 更新状态和缴费时间
        LocalDateTime now = LocalDateTime.now();
        for (Bill bill : pendingBills) {
            bill.setPayStatus(PayStatusEnum.PAID);
            bill.setPayTime(now);
        }

        // 4. 批量更新数据库
        this.updateBatchById(pendingBills);

        // 💡 进阶建议：可以在这里增加判断，如果该房间所有欠费已结清，
        // 则自动去修改 bus_room 的状态为 OCCUPIED
    }


    // Service 实现类中
    @Override
    public FinanceReportVO getFinanceReport(Integer year) {
        FinanceReportVO report = baseMapper.getFinanceReport(year);
        // 处理一下空数据的情况
        if (report.getTotalAmount() == null) {
            return new FinanceReportVO(); // 返回默认全0的对象
        }
        return report;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateMonthlyBill(LocalDate targetMonth, BigDecimal monthlyPrice) {
        // 1. 获取账单月份的1号和最后一天
        LocalDate billMonth = targetMonth.withDayOfMonth(1);
        LocalDate lastDay = targetMonth.with(TemporalAdjusters.lastDayOfMonth());

        // 2. 找出所有：已售/入住，且交房时间不晚于本月末的房间
        List<Room> rooms = roomMapper.selectList(new LambdaQueryWrapper<Room>()
                .in(Room::getStatus, 1, 2)
                .isNotNull(Room::getDeliveryDate)
                .le(Room::getDeliveryDate, lastDay));

        List<Bill> batch = new ArrayList<>();
        for (Room room : rooms) {
            // 3. 幂等性检查（改为与数据库唯一索引 uk_room_type_date 保持一致）
            boolean isExisted = this.count(new LambdaQueryWrapper<Bill>()
                    .eq(Bill::getRoomId, room.getId())
                    .eq(Bill::getType, 1) // 1-物业费
                    .eq(Bill::getBillDate, billMonth)) > 0;

            if (isExisted) continue;


            // 4. 确定该房间本月的实际计费起始日
            // 如果交房日期在月初前，则从月初1号收；如果在月中，则从交房日收。
            LocalDate actualStart = room.getDeliveryDate().isBefore(billMonth)
                    ? billMonth : room.getDeliveryDate();

            // 5. 计算金额
            BigDecimal amount;
            if (actualStart.isAfter(billMonth)) {
                // 折算：(月单价 * 面积 / 当月总天数) * 本月剩余天数
                long totalDays = ChronoUnit.DAYS.between(billMonth, lastDay) + 1;
                long chargeDays = ChronoUnit.DAYS.between(actualStart, lastDay) + 1;

                amount = room.getArea().multiply(monthlyPrice)
                        .divide(BigDecimal.valueOf(totalDays), 10, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(chargeDays))
                        .setScale(2, RoundingMode.HALF_UP);
            } else {
                // 全额收取
                amount = room.getArea().multiply(monthlyPrice).setScale(2, RoundingMode.HALF_UP);
            }

            // 6. 封装 Bill 对象并加入批量列表
            batch.add(createBillEntity(room, billMonth, actualStart, lastDay, amount, monthlyPrice));
        }

        if (!batch.isEmpty()) this.saveBatch(batch);
    }

    /**
     * 封装账单实体对象
     *
     * @param room         房间信息（获取ID、面积等）
     * @param billMonth    账单所属月份（通常是该月1号）
     * @param startDate    本笔账单实际计费的开始日期（考虑了交房日折算）
     * @param endDate      本笔账单计费结束日期（通常是月末）
     * @param amount       计算出来的最终应缴金额
     * @param monthlyPrice 当前执行的月单价（每平米单价）
     * @return 组装好的 Bill 对象
     */
    private Bill createBillEntity(Room room, LocalDate billMonth, LocalDate startDate,
                                  LocalDate endDate, BigDecimal amount, BigDecimal monthlyPrice) {
        Bill bill = new Bill();

        // 1. 基础关联信息
        bill.setRoomId(room.getId());
        bill.setType(1); // 费用类型：1-物业费

        // 2. 时间统计维度（方便按年、月进行 SQL 分组查询）
        bill.setYear(billMonth.getYear());
        bill.setMonth(billMonth.getMonthValue());

        // 3. 核心日期字段
        bill.setBillDate(billMonth);   // 账单月份标识
        bill.setStartDate(startDate);  // 计费起始日
        bill.setEndDate(endDate);      // 计费结束日

        // 4. 价格与金额
        bill.setUnitPrice(monthlyPrice);
        bill.setTotalAmount(amount);

        // 5. 状态管理
        bill.setPayStatus(PayStatusEnum.UNPAID); // 默认初始状态为“未缴费”

        // 6. 缴费截止日期（业务规则：账单月的次月15号 23:59:59）
        // 例如：生成5月的账单，截止日期就是 6月15日
        LocalDateTime deadline = billMonth.plusMonths(1)
                .withDayOfMonth(15)
                .atTime(23, 59, 59);
        bill.setDeadline(deadline);

        // 7. 审计字段（如果有父类 BaseEntity 会自动处理，如果没有则手动设置）
        // bill.setCreateTime(LocalDateTime.now());
        // bill.setIsDeleted(0);

        return bill;
    }

}
