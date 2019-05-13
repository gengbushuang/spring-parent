package com.annotation.beanfactory;

import com.annotation.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//配置类
@Configuration
public class BeanFactoryConfig {

    //容器注册一个bean，默认用方法名当作容器Id
    @Bean
    public Student student(){
        return new Student("张三",18);
    }

    //用名称studentFactory名字获取bean获取的是Student对象
    //要想获取StudentFactory对象要在ID前面加上&，&studentFactory
    @Bean
    public StudentFactory studentFactory(){
        return new StudentFactory();
    }
}
