package com.aijia.aijia.controller;

import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.entity.Unit;
import com.aijia.aijia.entity.vo.UnitVO;
import com.aijia.aijia.mapper.UnitMapper;
import com.aijia.aijia.service.IUnitService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 单元表 前端控制器
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@RestController
@RequestMapping("/unit")
public class UnitController {

    @Autowired
    private IUnitService unitService;
    @Autowired
    private UnitMapper unitMapper;

    @GetMapping("/page")
    public Result<Page<UnitVO>> findPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long buildingId) {

        // 调用 Service 层业务逻辑
        Page<UnitVO> result = unitService.findUnitPage(current, size, buildingId);

        return Result.success(result);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        unitService.deleteUnit(id);
        return Result.success(null);
    }
    @GetMapping("/list")
    public Result<List<Unit>> list(@RequestParam Long buildingId) {
        return Result.success(unitService.list(new LambdaQueryWrapper<Unit>()
                .eq(Unit::getBuildingId, buildingId)));
    }

}
