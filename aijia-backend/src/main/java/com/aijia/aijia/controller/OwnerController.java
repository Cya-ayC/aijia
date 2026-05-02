package com.aijia.aijia.controller;

import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.entity.Owner;
import com.aijia.aijia.service.IOwnerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 业主基础信息表 前端控制器
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private IOwnerService ownerService;

    // 分页查询业主
    @GetMapping("/page")
    public Result<Page<Owner>> findPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer sex,     // 性别通常是精确匹配
            @RequestParam(required = false) String idCard) { // 身份证模糊查询

        Page<Owner> page = new Page<>(current, size);
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<>();

        // 模糊查询：条件不为空时才拼接 SQL
        wrapper.like(StringUtils.isNotBlank(name), Owner::getName, name);
        wrapper.like(StringUtils.isNotBlank(phone), Owner::getPhone, phone);
        wrapper.like(StringUtils.isNotBlank(idCard), Owner::getIdCard, idCard);

        // 精确查询：性别
        wrapper.eq(sex != null, Owner::getSex, sex);

        wrapper.orderByDesc(Owner::getCreateTime);
        return Result.success(ownerService.page(page, wrapper));
    }


    // 新增业主
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Owner owner) {
        ownerService.addOwner(owner);
        return Result.success(null);
    }

    // 修改业主
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Owner owner) {
        ownerService.updateById(owner);
        return Result.success(null);
    }

    // 删除业主
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        ownerService.deleteOwner(id); // 调用业务层带校验的方法
        return Result.success(null);
    }

}
