package com.annotation.beanlife;

import com.annotation.model.Life;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanLifeTest {

    @Test
    public void lifeTest(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanLifeConfig.class);
        System.out.println("启动完成....");

        Life bean = applicationContext.getBean(Life.class);

        applicationContext.close();
    }
}
