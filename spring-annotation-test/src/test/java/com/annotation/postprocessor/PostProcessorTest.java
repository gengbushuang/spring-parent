package com.annotation.postprocessor;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PostProcessorTest {


    @Test
    public void testPostProcessor(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ProcessorConfig.class);
        System.out.println("启动完成....");
//        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
//        for(String name:beanDefinitionNames){
//            System.out.println(name);
//        }
        applicationContext.close();
    }
}
