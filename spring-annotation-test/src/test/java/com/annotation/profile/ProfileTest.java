package com.annotation.profile;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ProfileTest {

    @Test
    public void profileTest(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ProfileConfig.class);
        System.out.println("启动完成....");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String name:beanDefinitionNames){
            System.out.println(name);
        }
        applicationContext.close();
    }

    @Test
    public void profile2Test(){
        //启动参数指定 -Dspring.profilex.active=test

        //代码方式指定profile的环境参数
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.getEnvironment().setActiveProfiles("test","dev");
        applicationContext.register(ProfileConfig.class);
        applicationContext.refresh();

        System.out.println("启动完成....");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String name:beanDefinitionNames){
            System.out.println(name);
        }
        applicationContext.close();
    }
}
