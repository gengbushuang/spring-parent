package com.annotation.imp.selector;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ImportSelectorTest {

    @Test
    public void testImportSelector(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ImportSelectorConfig.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for(String name:beanDefinitionNames){
            System.out.println(name);
        }
    }
}
