package com.atguigu.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * EasyExcel读文件的实体类
 */
@Data
public class DemoData1 {
    @ExcelProperty(index = 0)
    private String sno;

    @ExcelProperty(index = 1)
    private String sname;
}
