package com.aijia.aijia.service.impl;

import com.aijia.aijia.common.enums.NoticeStatusEnum;
import com.aijia.aijia.entity.Notice;
import com.aijia.aijia.mapper.NoticeMapper;
import com.aijia.aijia.service.INoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 社区公告表 服务实现类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Override
    public void publishSystemNotice(String title, String content) {
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setType(1); // 全体公告
        notice.setStatus(NoticeStatusEnum.PUBLISHED); // 直接设为发布状态
        notice.setIsRead(0); // 默认未读
        // 这里的 authorId 可以通过 SecurityContext 获取当前登录管理员ID
        this.save(notice);
    }
}
