package com.annotation.aop;

import com.annotation.aops.AopConifg;
import com.annotation.aops.TestAop;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AopTest {

    @Test
    public void testAop() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AopConifg.class);
        System.out.println("启动完成....");

        TestAop testAop = context.getBean("testAop",TestAop.class);
//
        int div = testAop.div(6, 0);

        context.close();
    }
}
