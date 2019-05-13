package com.annotation.bean;

import com.annotation.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//配置类
@Configuration
public class BeanConfig {

    //容器注册一个bean，默认用方法名当作容器Id
    @Bean
    public Student student(){
        return new Student("张三",18);
    }
}
