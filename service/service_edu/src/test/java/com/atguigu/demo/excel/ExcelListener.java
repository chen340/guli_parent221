package com.atguigu.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * EasyExcel读操作的监听器
 */
public class ExcelListener extends AnalysisEventListener<DemoData1> {
    @Override
    public void invoke(DemoData1 data1, AnalysisContext analysisContext) {
        System.out.println("*****"+data1);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("------"+headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
