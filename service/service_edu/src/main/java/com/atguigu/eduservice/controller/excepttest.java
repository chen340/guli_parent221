package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.Result;
import com.atguigu.servicebase.handler.exceptionhandler.CustomException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("eduservice")
public class excepttest {

    @GetMapping("except")
    public Result test(){
        throw new CustomException(20001,"执行力自定义异常");

    }
}
