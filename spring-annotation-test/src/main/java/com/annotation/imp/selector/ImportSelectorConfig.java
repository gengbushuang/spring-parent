package com.annotation.imp.selector;

import com.annotation.model.Blank;
import com.annotation.model.Red;
import com.annotation.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//配置类
@Configuration
//除了用bean注入容器，包扫描注入容器，还可以使用Import来注入，Id是全类名名称
//
@Import({Blank.class,MyImportSelector.class})
public class ImportSelectorConfig {

    //容器注册一个bean，默认用方法名当作容器Id
    @Bean
    public Student student(){
        return new Student("张三",18);
    }
}
