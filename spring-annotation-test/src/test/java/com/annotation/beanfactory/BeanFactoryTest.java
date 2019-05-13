package com.annotation.beanfactory;

import com.annotation.model.Student;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanFactoryTest {

    @Test
    public void testFactory(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanFactoryConfig.class);
        System.out.println("启动完成....");

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for(String name:beanDefinitionNames){
            System.out.println(name);
        }

        Object studentFactory = context.getBean("studentFactory");
        System.out.println(studentFactory.getClass());

        Object factory = context.getBean("&studentFactory");
        System.out.println(factory.getClass());
    }
}
