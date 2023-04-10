package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.feign.VodFeign;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-03-03
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodFeign vodFeign;

    //根据课程id删除小节
    @Override
    public void removeVideoByCourseId(String courseId) {
        //删除小节前需要先删除小节下的视频文件
        //TODO 删除小节前需要先删除小节下的视频文件
        //1.根据课程id查询课程中所有的视频id(视频id在小节表中,所以我们查小节表edu_video)
        //①得到List<EduVideo>
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        wrapperVideo.select("video_source_id"); //查询指定的列
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);
        //②将List<EduVideo>变为List<String>
        List<String> videoIds = new ArrayList<>();
        for (int i = 0; i < eduVideoList.size(); i++) {
            EduVideo eduVideo = eduVideoList.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            //将不为空的视频id放到videoIds集合中
            if (!StringUtils.isEmpty(videoSourceId)) {
                videoIds.add(videoSourceId);
            }
        }
        //2.远程调用:根据多个视频id删除阿里云中的视频
        //判断:如果课程下没有一个视频,那就不用调用这个方法了
        if (videoIds.size() > 0) {
            vodFeign.deleteBatch(videoIds);
        }
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }
}
