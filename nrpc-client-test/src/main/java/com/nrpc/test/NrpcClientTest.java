package com.nrpc.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import test.com.springboot.autoconfig.nrpc.client.NrpcClient;

@Service
public class NrpcClientTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NrpcClientTest.class);

    @NrpcClient()
    private HelloService helloService;

    public void testHelo(String name) {
        String hello = helloService.hello(name);
        LOGGER.info("testHelo------>" + hello);
    }
}
