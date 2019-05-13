package com.annotation.bean;

import com.annotation.model.Student;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanTest {

    @Test
    public void testBean(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfig.class);
        Student student = applicationContext.getBean(Student.class);
        System.out.println(student);

        String[] beanNamesForType = applicationContext.getBeanNamesForType(Student.class);
        for (String name : beanNamesForType) {
            System.out.println(name);
        }
    }
}
