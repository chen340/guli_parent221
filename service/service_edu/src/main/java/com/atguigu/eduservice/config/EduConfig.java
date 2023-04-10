package com.atguigu.eduservice.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 僵尸模块配置类
 */

@Configuration
@MapperScan("com.atguigu.eduservice.mapper")
public class EduConfig {

    //逻辑删除插件
    @Bean
    public ISqlInjector injector(){
        return new LogicSqlInjector();
    }

    //mp分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
