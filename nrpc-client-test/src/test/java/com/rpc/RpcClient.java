package com.rpc;

import com.nrpc.NrpcApplication;
import com.nrpc.test.NrpcClientTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = NrpcApplication.class)
public class RpcClient {

    @Autowired
    private NrpcClientTest nrpcClientTest;

    @Test
    public void testRpc(){
        for(int i = 0;i<10;i++) {
            nrpcClientTest.testHelo("你好!");
        }
    }

}
