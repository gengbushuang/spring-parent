package com.atoconfigure.test;

import com.actoconfigure.test.ActoconfigureTest;
import com.actoconfigure.test.test.ServiceHandler;
import io.grpc.Server;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ActoconfigureTest.class)
public class ServiceBeanFindTest {

    @Autowired
    private ServiceHandler serviceHandler;

    @Test
    public void testFindName(){
        Collection<String> serviceBeanNames = serviceHandler.findServiceBeanNames();
        System.out.println(serviceBeanNames);
    }

    @Test
    public void testGrpcServiceName(){
        serviceHandler.findGrpcServices();
    }
    
    @Test
    public void testServer() throws IOException, InterruptedException {
        Server server = serviceHandler.crateService();
        server.start();

        if (server != null) {
            server.awaitTermination();
        }
    }





}
