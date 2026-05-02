package com.aijia.aijia.controller;

import com.aijia.aijia.common.enums.RepairStatusEnum;
import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.common.utils.SecurityUtils;
import com.aijia.aijia.entity.Owner;
import com.aijia.aijia.entity.Repair;
import com.aijia.aijia.entity.dto.RepairQueryDTO;
import com.aijia.aijia.entity.vo.RepairVO;
import com.aijia.aijia.mapper.sys.SysRoleMapper;
import com.aijia.aijia.service.IOwnerService;
import com.aijia.aijia.service.IRepairService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair")
public class RepairController {

    @Autowired
    private IRepairService repairService;
    @Autowired
    private IOwnerService ownerService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    // 1. 业主申请报修
    @PostMapping("/apply")
    public Result<Void> apply(@RequestBody Repair repair) {
        // 1. 获取当前登录的系统用户 ID (sys_user 表的 ID)
        Long userId = SecurityUtils.getUserId();

        // 2. 🚀 关键步骤：根据 userId 去业主表查真正的 ownerId
        // 假设你已经注入了 ownerService
        Owner owner = ownerService.getOne(new LambdaQueryWrapper<Owner>()
                .eq(Owner::getUserId, userId));

        if (owner == null) {
            return Result.error("当前登录用户不是有效的业主，无法报修");
        }

        // 3. 绑定真正的业主主键 ID
        repair.setOwnerId(owner.getId());
        repair.setStatus(RepairStatusEnum.PENDING);
        repair.setContactPhone(owner.getPhone());

        repairService.save(repair);
        return Result.success();
    }


    // 2. 分页查询报修列表 (VO 对象)
    @GetMapping("/page")
    public Result<IPage<RepairVO>> getPage(Page page, RepairQueryDTO query) {
        // 1. 获取当前登录系统用户 ID
        Long userId = SecurityUtils.getUserId();

        // 2. 🚀 直接从数据库查出该用户的所有角色 Key 列表（最准确）
        List<String> roles = sysRoleMapper.getRoleKeysByUserId(userId);

        // 3. 根据角色现场判断过滤条件
        if (roles.contains("REPAIR_MAN")) {
            // 如果是维修工：只能看指派给自己的单子
            query.setHandlerId(userId);
        }
        else if (roles.contains("OWNER")) {
            // 如果是业主：只能看自己报修的单子
            // 注意：这里要用 userId 换取真正的 ownerId（因为 repair 表存的是 owner_id）
            Owner owner = ownerService.getOne(new LambdaQueryWrapper<Owner>()
                    .eq(Owner::getUserId, userId));
            if (owner != null) {
                query.setOwnerId(owner.getId());
            } else {
                // 如果是业主角色但没绑定房产，返回空数据或报错
                return Result.success(new Page<>());
            }
        }
        // 如果是 ADMIN（管理员），不会进以上判断，query 里的过滤 ID 为空，查出全部数据

        return Result.success(repairService.selectRepairPage(page, query));
    }



    // 3. 管理员派单/处理
    @PutMapping("/handle")
    public Result<Void> handle(@RequestBody Repair repair) {
        repairService.handleRepair(repair);
        return Result.success();
    }

    /**
     * 确认报修完成
     * @param id 报修单ID
     */
    @PutMapping("/complete/{id}")
    public Result<Void> completeRepair(@PathVariable Long id) {
        // 调用 service 层逻辑，记录完成时间并修改状态
        repairService.completeRepair(id);
        return Result.success();
    }



}
