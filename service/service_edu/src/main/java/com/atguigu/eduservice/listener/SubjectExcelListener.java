package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.handler.exceptionhandler.CustomException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    /**
     * 监听器不能直接操作数据库，所有创建一个EduSubjectService属性，并创建一个有参构造函数将传进来的eduSubjectService注入到监听器种
     * @param subjectData
     * @param analysisContext
     */

    private EduSubjectService subjectService;

    public SubjectExcelListener() {
    }
    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.subjectService = eduSubjectService;
    }

    //一行一行读取excel内容(从第二行开始读取,第一行是表头,该方法不会读取第一行，先读一级分类，再读二级分类)
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {

        /**因为分类的标题不能有重复，比如一级分类中有前端开发、后端开发、数据库等，二级分类中的前端开发中有vue、JavaScript、jquery等
         * 1、先判断数据库中是否存在一级分类，不存在则读（写入数据库中）
         * 2、先判断数据库中是否存在二级分类，不存在则读（写入数据库中）
         */
        //判断文件是否为空，为空则抛异常
        if (subjectData == null) throw new CustomException(20001,"文件为空");

        //读取一级分类(excel表的第一列)，调用是否重复的方法判断，不重复(为空)则读（写入数据库）
        EduSubject existOneSubject = this.existOneSubject(subjectService, subjectData.getOneSubjectName());
        if (existOneSubject == null){
            System.out.println("---------------"+subjectData);
            existOneSubject = new EduSubject();
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            existOneSubject.setParentId("0");
            subjectService.save(existOneSubject);   //写入数据库
        }
        //读取二级分类（excel表的第二列）
        String pid = existOneSubject.getId();       //获取第一列在数据库中的id作为第二列的parent_id
        EduSubject existTwoSubject = this.existTwoSubject(subjectService, pid, subjectData.getTwoSubjectName());
        if (existTwoSubject == null){
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());
            subjectService.save(existTwoSubject);
        }

    }

    //判断是否有一级分类重复的方法
    private EduSubject existOneSubject(EduSubjectService eduSubjectService, String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject one = eduSubjectService.getOne(wrapper);
        return one;
    }
    //判断是否有二级分类重复的方法
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService, String pid, String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject one = eduSubjectService.getOne(wrapper);
        return one;
    }
    //读取完成之后执行这个方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
