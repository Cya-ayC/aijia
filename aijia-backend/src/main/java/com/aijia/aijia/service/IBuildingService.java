package com.aijia.aijia.service;

import com.aijia.aijia.common.resultful.Result;
import com.aijia.aijia.entity.Building;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 楼栋信息表 服务类
 * </p>
 *
 * @author yongan
 * @since 2026-04-25
 */
public interface IBuildingService extends IService<Building> {
    void addBuilding(Building building);
    void deleteBuilding(Long id);
    void updateBuilding(Building building);
    void importBuildings(MultipartFile file);

}
