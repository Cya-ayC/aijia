package com.aijia.aijia.service;

import com.aijia.aijia.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 社区公告表 服务类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface INoticeService extends IService<Notice> {

    /** 发布一条系统级全体公告 */
    void publishSystemNotice(String title, String content);
}
