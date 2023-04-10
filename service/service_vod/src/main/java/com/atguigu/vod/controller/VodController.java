package com.atguigu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.commonutils.Result;
import com.atguigu.servicebase.handler.exceptionhandler.CustomException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {
    @Autowired
    private VodService vodService;

    //上传视频到阿里云
    @PostMapping("uploadAlyVideo")
    public Result uploadAlyVideo(MultipartFile file) {
        //返回上传视频的id
        String videoId = vodService.uploadAlyVideo(file);
        return Result.OK().data("videoId", videoId);
    }
    //根据视频id删除阿里云中的视频
    @DeleteMapping("removeAlyVideo/{id}")
    public Result removeAlyVideo(@PathVariable String id) {
        try {
            //1.创建初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(
                    ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.ACCESS_KEY_SECRET);
            //2.创建删除的request
            DeleteVideoRequest request = new DeleteVideoRequest();
            //3.向request对象里面设置视频id
            request.setVideoIds(id);
            //4.调用初始化对象里面的方法,实现删除
            client.getAcsResponse(request);
            return Result.OK();
        } catch(Exception e) {
            e.printStackTrace();
            throw new CustomException(20001, "删除视频失败");
        }
    }

    //删除多个视频
    @DeleteMapping("delete-batch")
    public Result deleteBatch(@RequestParam("videoIdList") List<String > videoIdList){
        vodService.removeMoreAlyVideo(videoIdList);
        return Result.OK();
    }

}


