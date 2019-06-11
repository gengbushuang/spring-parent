package com.nrpc.test;

import test.com.springboot.autoconfig.nrpc.server.NrpcServer;

@NrpcServer(HelloService.class) // 指定远程接口
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }
}