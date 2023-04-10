package com.atguigu.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

//课程分类实体
@Data
public class SubjectData {

    //excel第一列对应的属性
    @ExcelProperty(index = 0)
    private String oneSubjectName;

    //excel第二列对应的属性
    @ExcelProperty(index = 1)
    private String twoSubjectName;
}
