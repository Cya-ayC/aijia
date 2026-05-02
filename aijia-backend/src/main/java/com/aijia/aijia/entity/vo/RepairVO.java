package com.aijia.aijia.entity.vo;

import com.aijia.aijia.entity.Repair;
import lombok.Data;

@Data
public class RepairVO extends Repair {
    private String buildingName; // 楼栋名
    private String unitName;     // 单元名
    private String roomNum;      // 房号
    private String ownerName;    // 报修业主姓名
    private String ownerPhone;   // 业主手机
}

