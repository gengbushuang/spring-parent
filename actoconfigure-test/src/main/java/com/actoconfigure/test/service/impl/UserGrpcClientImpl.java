package com.actoconfigure.test.service.impl;

import com.actoconfigure.test.model.rpc_package.HelloReply;
import com.actoconfigure.test.model.rpc_package.HelloRequest;
import com.actoconfigure.test.model.rpc_package.HelloWorldServiceGrpc;
import com.actoconfigure.test.service.GrpcClient;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Service;

@Service
public class UserGrpcClientImpl implements GrpcClient {

    @test.com.springboot.autoconfig.grpc.client.GrpcClient(value = "GRPC-SERVER")
    private Channel channelUser;

    public void method(String name){
        HelloWorldServiceGrpc.HelloWorldServiceBlockingStub blockingStub =HelloWorldServiceGrpc.newBlockingStub(channelUser);
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            return;
        }
        System.out.println("------>Greeting: " + response.getMessage());
    }
}
