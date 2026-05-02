package com.aijia.aijia.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomImportDTO {
    @ExcelProperty("楼栋名称")
    private String buildingName;

    @ExcelProperty("单元名称")
    private String unitName;

    @ExcelProperty("房间号")
    private String roomNum;

    @ExcelProperty("所在楼层")
    private Integer floor;

    @ExcelProperty("建筑面积")
    private BigDecimal area;

    @ExcelProperty("房间状态") // Excel 中的列名，用户填：闲置、已售 或 入住
    private String statusName;

    @ExcelProperty("备注")
    private String remark;
}
