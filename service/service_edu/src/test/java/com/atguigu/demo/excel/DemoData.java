package com.atguigu.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * EasyExcel写文件的实体类
 */
@Data
public class DemoData {

    @ExcelProperty("学生id")
    private Integer sno;

    @ExcelProperty("学生名称")
    private String sname;
}
