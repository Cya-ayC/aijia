package com.aijia.aijia.controller;

import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.entity.Building;
import com.aijia.aijia.service.IBuildingService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    private IBuildingService buildingService;

    /**
     * 查询所有楼栋 (不分页)
     */
    @GetMapping("/list")
    public Result<List<Building>> list() {
        return Result.success(buildingService.list());
    }

    /**
     * 新增楼栋 (包含重复校验逻辑)
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Building building) {
        buildingService.addBuilding(building);
        return Result.success(null);
    }

    /**
     * 修改楼栋
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Building building) {
        // 直接调用 MyBatis-Plus 的 updateById
        // 如果需要修改时也校验重名，可以在 Service 层增加 updateBuilding 方法
        buildingService.updateBuilding(building);
        return Result.success(null);
    }

    /**
     * 删除楼栋 (包含级联校验逻辑)
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return Result.success(null);
    }

    /**
     * 分页查询楼栋
     * @param current 当前页
     * @param size 每页显示条数
     * @param name 可选：按楼栋名称模糊搜索
     */
    @GetMapping("/page")
    public Result<Page<Building>> findPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {

        // 1. 创建分页对象
        Page<Building> page = new Page<>(current, size);

        // 2. 构建查询条件
        LambdaQueryWrapper<Building> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(Building::getName, name); // 支持按名称模糊查询
        }
        wrapper.orderByAsc(Building::getId);

        // 3. 执行分页查询
        Page<Building> resultPage = buildingService.page(page, wrapper);

        return Result.success(resultPage);
    }

    @PostMapping("/import")
    public Result<Void> importExcel(@RequestParam("file") MultipartFile file) {
        buildingService.importBuildings(file);
        return Result.success(null);
    }

}
