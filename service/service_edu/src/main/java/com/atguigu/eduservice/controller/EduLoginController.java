package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("eduservice/user")
@CrossOrigin    //解决跨域问题
public class EduLoginController {

    @PostMapping("login")
    public Result login(){
        return Result.OK().data("token","admin");
    }

    @GetMapping("info")
    public Result info(){
        return Result.OK().data("roles","admin").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
