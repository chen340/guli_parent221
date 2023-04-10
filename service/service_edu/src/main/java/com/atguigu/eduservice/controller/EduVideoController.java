package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.feign.VodFeign;
import com.atguigu.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-03-03
 */
@RestController
@CrossOrigin
@RequestMapping("/eduservice/video")
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VodFeign vodFeign;

    //添加小结
    @PostMapping("addVideo")
    public Result addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return Result.OK();
    }
    //删除小节
    @DeleteMapping("{id}")
    public Result deleteVideo(@PathVariable String id) {
        //删除视频
        EduVideo byId = eduVideoService.getById(id);
        String videoSourceId = byId.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)){
            vodFeign.removeAlyVideo(videoSourceId);
            System.out.println("执行了rpc，删除了视频");
        }
        //删除小结
        eduVideoService.removeById(id);
        return Result.OK();
    }

    //根据小节id查询
    @GetMapping("getVideoInfo/{videoId}")
    public Result getVideoInfo(@PathVariable String videoId) {
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return Result.OK().data("video", eduVideo);

    }

    //修改小节
    @PostMapping("updateVideo")
    public Result updateVideo(@RequestBody EduVideo eduVideo) {
        eduVideoService.updateById(eduVideo);
        return Result.OK();
    }

}

