package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.handler.exceptionhandler.CustomException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-03
 */
//根据课程id查询返回所有章节、小结
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //1、根据课程id查询所有章节
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id",courseId);
        List<EduChapter> eduChapterslist = baseMapper.selectList(eduChapterQueryWrapper);
        //2、根据课程id查询所有小结
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id",courseId);
        List<EduVideo> eduvideolist = eduVideoService.list(eduVideoQueryWrapper);
        //遍历查询出的章节的集合，封装到ChapterVo中
        List<ChapterVo> finalchapterVos = new ArrayList<>();
        for (int i = 0; i < eduChapterslist.size(); i++) {
            EduChapter eduChapter = eduChapterslist.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalchapterVos.add(chapterVo);
            //遍历查询到的小节的集合，将章节中一样的小结封装到VideoVo中
            List<VideoVo> videoVoslist = new ArrayList<>();
            for (int j = 0; j <eduvideolist.size(); j++) {
                EduVideo eduVideo = eduvideolist.get(j);
                if (eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoslist.add(videoVo);
                }
                //将章节中的小结封装
                chapterVo.setChildren(videoVoslist);
            }
        }
        return finalchapterVos;
    }
    //删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据章节id(chapterid)查询小节表(edu_video),若能查到数据,就不删除该章节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        int count = eduVideoService.count(wrapper); //返回符合条件的数据的个数
        //判断
        if (count > 0) { //该章节下有小节,不删除该章节
            throw new CustomException(20001, "该章节下面还有小节，请先删除完小节再删除章节");
        } else { //该章节下没有小节,删除该章节
            int result = baseMapper.deleteById(chapterId);
            return  result > 0;
        }
    }
    //根据课程id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }

}
