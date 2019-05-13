package com.annotation.imp.registrar;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ImportRegistrarTest {

    @Test
    public void testRegistrar(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ImportRegistrarConfig.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for(String name:beanDefinitionNames){
            System.out.println(name);
        }
    }
}
