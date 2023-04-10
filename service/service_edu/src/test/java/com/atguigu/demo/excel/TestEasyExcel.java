package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyExcel写文件
 */
public class TestEasyExcel {
    public static void main(String[] args) {
        String filename = "e:\\3.1.xlsx";
        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getDemo());
    }

    private static List<DemoData> getDemo(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10 ; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("老王"+1);
            list.add(data);

        }
        return list;
    }

}
