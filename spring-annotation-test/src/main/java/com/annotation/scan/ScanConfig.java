package com.annotation.scan;

import com.annotation.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

//配置类
@Configuration
//包扫描 @Controller,@Service,@Repository,@Component
//value-->指定要扫描的包
//excludeFilters-->排除要扫描的规则

//CUSTOM自定义过滤规则
@ComponentScan(value="com.annotation.scan",excludeFilters={
//    @ComponentScan.Filter(type=FilterType.ANNOTATION,classes={Controller.class,Service.class})
    @ComponentScan.Filter(type=FilterType.CUSTOM,classes={MyTypeFilter.class})

})
public class ScanConfig {

    //容器注册一个bean，默认用方法名当作容器Id
    @Bean
    public Student student(){
        return new Student("张三",18);
    }
}
