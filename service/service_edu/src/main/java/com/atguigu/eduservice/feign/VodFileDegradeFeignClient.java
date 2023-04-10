package com.atguigu.eduservice.feign;

import com.atguigu.commonutils.Result;

import java.util.List;

public class VodFileDegradeFeignClient implements VodFeign{
    @Override
    public Result removeAlyVideo(String id) {
        return Result.error().message("删除单个视频失败了");
    }

    @Override
    public Result deleteBatch(List<String> videoIdList) {
        return Result.error().message("删除多个视频失败了");
    }
}
