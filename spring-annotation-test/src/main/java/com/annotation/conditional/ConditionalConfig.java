package com.annotation.conditional;

import com.annotation.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

//配置类
@Configuration
public class ConditionalConfig {

    //容器注册一个bean，默认用方法名当作容器Id
    //Conditional按照一定的条件给容器注入bean
    @Bean
    @Conditional(LinuxCondition.class)
    public Student studentLinux(){
        return new Student("linux",0);
    }

    @Bean
    @Conditional(WindowsCondition.class)
    public Student studentWin(){
        return new Student("windows",0);
    }
}
