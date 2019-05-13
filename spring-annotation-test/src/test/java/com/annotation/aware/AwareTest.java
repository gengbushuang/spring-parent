package com.annotation.aware;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AwareTest {

    @Test
    public void awareTest(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AwareConfig.class);
        System.out.println("启动完成....");

        applicationContext.close();
    }
}
