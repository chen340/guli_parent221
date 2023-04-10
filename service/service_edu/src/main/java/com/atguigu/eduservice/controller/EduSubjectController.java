package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-02
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
   private EduSubjectService subjectService;
    /**
     * 添加课程分类，获得上传过来的文件读取并写入数据库
     * @return
     */
    @PostMapping("addSubject")
    public Result addSubject(MultipartFile file){
        subjectService.saveSubject(file,subjectService);
        return Result.OK();
    }
    //课程分类列表（树形）
    @GetMapping("getAllSubject")
    public Result getAllSubject(){
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return Result.OK().data("list",list);
    }
}

