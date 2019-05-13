package com.annotation.imp.registrar;

import com.annotation.imp.selector.MyImportSelector;
import com.annotation.model.Blank;
import com.annotation.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//配置类
@Configuration
//除了用bean注入容器，包扫描注入容器，还可以使用Import来注入，Id是全类名名称
//ImportBeanDefinitionRegistrar可以手动注册bean到容器里面
@Import({Blank.class,MyImportSelector.class,MyImportBeanDefinitionRegistrar.class})
public class ImportRegistrarConfig {

    //容器注册一个bean，默认用方法名当作容器Id
    @Bean
    public Student student(){
        return new Student("张三",18);
    }
}