package com.atoconfigure.test;

import com.actoconfigure.test.ActoconfigureTest;
import com.actoconfigure.test.service.GrpcClient;
import com.actoconfigure.test.test.ClientHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ActoconfigureTest.class)
public class ClientBeanFindTest {

    @Autowired
    private GrpcClient grpcClient;

    @Test
    public void test () throws InterruptedException {
        String name= "gbs";
        grpcClient.method(name);
        System.out.println("fffff");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
