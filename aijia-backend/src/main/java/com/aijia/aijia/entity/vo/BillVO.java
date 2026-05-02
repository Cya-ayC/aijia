package com.aijia.aijia.entity.vo;

import com.aijia.aijia.entity.Bill;
import lombok.Data;

@Data
public class BillVO extends Bill {
    private String buildingName;
    private String unitName;
    private String roomNum;
    private String ownerName;
}
