package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

public class TestEasyExcelRead {
    public static void main(String[] args) {
        String filename = "e:\\3.1.xlsx";
        EasyExcel.read(filename,DemoData1.class,new ExcelListener()).sheet().doRead();
    }
}
