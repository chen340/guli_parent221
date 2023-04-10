package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.handler.exceptionhandler.CustomException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-02-21
 */
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    /**
     * 查询所有讲师
     * @return
     */
    @ApiOperation(value = "查询所有讲师")
    @GetMapping("findAll")
    public Result findALLTeacher(){

        List<EduTeacher> list = eduTeacherService.list(null);
        return Result.OK().data("items",list);
    }
    /**
     * 根据id删除讲师
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id删除讲师")
    @DeleteMapping("{id}")          //{id}表示传入的id参数 路径为 localhost:8001//eduservice/teacher?id
    public Result removeTeacher(@ApiParam(name = "id",value = "讲师id",required = true) @PathVariable String id){

        boolean b = eduTeacherService.removeById(id);
        if (b){
            return Result.OK().message("删除成功");
        }else {
            return Result.error();
        }
    }
    /**
     * 分页查询讲师数据
     */
    @ApiOperation(value = "分页查询讲师")
    @GetMapping("pageTeacher/{current}/{limit}")
    public Result pageTeacher(@PathVariable Long current,@PathVariable Long limit){
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        //分页插件会将查询结果封装到pageTeacher中
        eduTeacherService.page(pageTeacher, null);
        long total = pageTeacher.getTotal();//总记录数
        long current1 = pageTeacher.getCurrent();//当前页记录数
        List<EduTeacher> records = pageTeacher.getRecords();    //当前页数据
        return Result.OK().data("total",total).data("current1",current1).data("items",records);
    }
    /**
     * 分页条件组合模糊查询
     */
    @ApiOperation(value = "分页组合模糊查询")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public Result pageTeacherCondition(@PathVariable Long current,
                                       @PathVariable Long limit,
                                       @RequestBody(required = false) TeacherQuery teacherQuery){
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        //创建条件构造器
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //获取前端传来的值组成条件
        if(teacherQuery != null){
            String name = teacherQuery.getName();
            Integer level = teacherQuery.getLevel();
            String begin = teacherQuery.getBegin();
            String end = teacherQuery.getEnd();
            if (!StringUtils.isEmpty(name)){
                wrapper.like("name",name);
            }
            if (!StringUtils.isEmpty(level)){
                wrapper.eq("level",level);
            }
            if (!StringUtils.isEmpty(begin)){
                wrapper.ge("gmt_create",begin);
            }
            if (!StringUtils.isEmpty(end)){
                wrapper.le("gmt_create",end);
            }
        }
        wrapper.orderByDesc("gmt_create");
        eduTeacherService.page(pageTeacher,wrapper);
        long current1 = pageTeacher.getCurrent();       //当前页数
        long total = pageTeacher.getTotal();            //总记录数
        List<EduTeacher> records = pageTeacher.getRecords();    //当前页数据
        return Result.OK().data("current1",current1).data("total",total).data("items",records);
    }
    /**
     * 添加讲师
     */
    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public Result addTeacher(@RequestBody(required = false) EduTeacher eduTeacher){
        //根据前端传来的对象获取名称，查询数据库是否已经有此对象，是则添加失败，否则添加成功
        String newname = eduTeacher.getName();
        //获取条件查询包装器并且查询用户
        QueryWrapper<EduTeacher> warpper = new QueryWrapper<>();
        EduTeacher teacher = eduTeacherService.getOne(warpper.eq("name", newname));
     //如果teacher不存在则会报空指针异常，所以要进行非空判断
       if (teacher!= null && teacher.getName().equals(newname)) {
           return Result.error().message("添加失败，已经拥有此用户");
       }
        eduTeacherService.save(eduTeacher);
        return Result.OK().message("添加成功");
    }
    /**
     * 根据id查询用户
     */
    @ApiOperation(value = "根据id查询")
    @GetMapping("getTeacher/{id}")
    public Result getTeacher(@PathVariable String id){
        EduTeacher teacher = eduTeacherService.getById(id);
        if (teacher != null){
            return Result.OK().data("teacher",teacher).message("查询成功");
        }else {
            return Result.error().message("没有该用户");
        }
    }
    /**
     * 根据id修改用户
     */
    @ApiOperation(value = "根据id修改用户")
    @PutMapping(value = "updateTeacher")
    public Result updateTeacher(@RequestBody(required = false) EduTeacher eduTeacher){
        boolean b = eduTeacherService.updateById(eduTeacher);
        if (b){
            return Result.OK().message("修改成功");
        }else {
            return Result.error().message("修改失败");
        }
    }
    /**
     * 全局异常处理测试类
     */
    @ApiOperation(value = "全局异常处理测试")
    @GetMapping("GloExceptiontest")
    public Result GloExceptiontest(){
        int a = 1/0;
        return Result.error();
    }
    /**
     * 自定义异常处理
     */
    @ApiOperation(value = "自定义异常")
    @GetMapping("CustomException")
    public Result CustomException(){

        throw new CustomException(5003,"抛出来自定义异常");
    }
}

