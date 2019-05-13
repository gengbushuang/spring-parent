package com.actoconfigure.test.service;

import com.actoconfigure.test.model.rpc_package.HelloReply;
import com.actoconfigure.test.model.rpc_package.HelloRequest;
import com.actoconfigure.test.model.rpc_package.HelloWorldServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;
import test.com.springboot.autoconfig.grpc.server.GrpcService;

@GrpcService(HelloWorldServiceGrpc.class)
public class UserService extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
