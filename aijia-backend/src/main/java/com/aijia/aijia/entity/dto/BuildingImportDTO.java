package com.aijia.aijia.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class BuildingImportDTO {
    @ExcelProperty("楼栋名称") // 对应 Excel 里的列名
    private String name;

    @ExcelProperty("单元数量")
    private Integer unitCount;

    @ExcelProperty("总层数")
    private Integer floorCount;

    @ExcelProperty("备注")
    private String remark;
}
