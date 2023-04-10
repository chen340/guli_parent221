package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.handler.exceptionhandler.CustomException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-03
 */

@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;
    //注入小节和章节service
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private EduChapterService chapterService;

    /**
     * 将数据分别保存在edu_course和edu_course_description中
     * @param courseInfoVo
     * @return
     */
    @Override
    @Transactional      //添加事务
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {

        //因为eduCourse中没有课程课程描述，所以不能直接添加
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int res = baseMapper.insert(eduCourse);
        if(res <= 0) throw new CustomException(20001,"添加课程失败");

        //往课程描述表中插入数据
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo,eduCourseDescription);
        eduCourseDescription.setId(eduCourse.getId());
        boolean save = eduCourseDescriptionService.save(eduCourseDescription);
        if (!save) throw new CustomException(20001,"添加失败");
        return eduCourse.getId();
    }

    //根据课程id获取课程信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        EduCourse eduCourse = baseMapper.selectById(courseId);
        System.out.println("----------basemapper----------"+eduCourse);
        EduCourse byId = this.getById(courseId);
        System.out.println("--------this--------"+byId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        EduCourseDescription byId1 = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(byId1.getDescription());
        return courseInfoVo;
    }

    //修改课程信息
    @Override
    @Transactional
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int i = baseMapper.updateById(eduCourse);
        if(i <= 0) throw new CustomException(20001,"更新课程信息失败");
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());   //根据id修改，所有必须要有id
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.updateById(eduCourseDescription);
    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    @Override
    @Transactional
    public void removeCourse(String courseId) {
        /**
         * 1、根据课程id删除小节
         * 2、根据课程id删除章节
         * 3、根据课程id删除课程描述
         * 4、根据课程id删除课程
         */
        eduVideoService.removeVideoByCourseId(courseId);
        chapterService.removeChapterByCourseId(courseId);
        eduCourseDescriptionService.removeById(courseId);
        int i = baseMapper.deleteById(courseId);
        if (i == 0){
            throw new CustomException(20001,"删除课程失败");
        }
    }
}
