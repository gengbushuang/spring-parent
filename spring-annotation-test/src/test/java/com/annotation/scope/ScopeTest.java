package com.annotation.scope;

import com.annotation.model.Student;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ScopeTest {

    @Test
    public void singletonTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScopeConfig.class);
        System.out.println("启动完成....");
        Student student1 = (Student) context.getBean("student");
        Student student2 = (Student) context.getBean("student");
    }

    @Test
    public void prototypeTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScopeConfig.class);
        Student student1 = (Student) context.getBean("student");
        Student student2 = (Student) context.getBean("student");
    }
}
