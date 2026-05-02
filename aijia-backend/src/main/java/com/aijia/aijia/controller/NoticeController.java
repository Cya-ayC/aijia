package com.aijia.aijia.controller;

import com.aijia.aijia.common.enums.NoticeStatusEnum;
import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.entity.Notice;
import com.aijia.aijia.service.INoticeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 社区公告表 前端控制器
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    /**
     * 1. 分页查询
     */
    @GetMapping("/page")
    public Result<IPage<Notice>> getPage(Page<Notice> page, String title) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<Notice>()
                .like(StringUtils.hasText(title), Notice::getTitle, title)
                .orderByDesc(Notice::getCreateTime);
        return Result.success(noticeService.page(page, wrapper));
    }

    /**
     * 2. 新增公告（管理员手动发布）
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody Notice notice) {
        notice.setIsRead(0); // 默认未读
        // status 可以在前端传，或者默认为发布状态
        noticeService.save(notice);
        return Result.success("公告已创建");
    }

    /**
     * 3. 修改公告
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Result.success("修改成功");
    }

    /**
     * 4. 状态切换（发布/撤回）
     */
    @PutMapping("/status/{id}/{status}")
    public Result<String> changeStatus(@PathVariable Long id, @PathVariable Integer status) {
        Notice notice = new Notice();
        notice.setId(id);
        // 这里根据你的枚举类进行设置
        notice.setStatus(NoticeStatusEnum.decode(status));
        noticeService.updateById(notice);
        return Result.success("操作成功");
    }

    /**
     * 5. 删除
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        noticeService.removeById(id);
        return Result.success("公告已删除");
    }
}
