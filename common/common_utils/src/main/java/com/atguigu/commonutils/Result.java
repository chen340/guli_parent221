package com.atguigu.commonutils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String,Object> data = new HashMap<>();

    //构造方法私有
    private Result(){
    }

    //成功的方法
    public static Result OK(){
        Result R = new Result();
        R.setSuccess(true);
        R.setCode(ResultCode.SUCCESS);
        R.setMessage("成功");
        return R;
    }

    //成失败的方法
    public static Result error(){
        Result R = new Result();
        R.setSuccess(false);
        R.setCode(ResultCode.ERROR);
        R.setMessage("失败");
        return R;
    }

    /**
     * 自定义Result   链式编程
     */

    //是否成功
    public Result success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    //状态码
    public Result code(Integer code){
        this.setCode(code);
        return this;
    }

    //返回信息
    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    //数据
    public Result data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    //数据
    public Result data(Map map){
        this.setData(map);
        return this;
    }
}
