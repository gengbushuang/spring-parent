package com.rpc;

import com.actoconfigure.test.model.rpc_package.HelloReply;
import com.actoconfigure.test.model.rpc_package.HelloRequest;
import com.actoconfigure.test.model.rpc_package.HelloWorldServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

public class RpcClient {
    private final ManagedChannel channel;
    private final HelloWorldServiceGrpc.HelloWorldServiceBlockingStub blockingStub;
    private final HelloWorldServiceGrpc.HelloWorldServiceStub serviceStub;

    public RpcClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true));
    }

    public RpcClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        this.blockingStub = HelloWorldServiceGrpc.newBlockingStub(channel);
        this.serviceStub = HelloWorldServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void greet(String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            return;
        }
        System.out.println("------>Greeting: " + response.getMessage());
    }

    public static void main(String[] args) throws InterruptedException {
//        RpcClient client = new RpcClient("localhost", 9090);
//        String user = "world";
//        try {
//            client.greet(user);
//        } finally {
//            client.shutdown();
//        }
        boolean b = false;
        try {
            if(b){
                return;
            }
            System.out.println("bbbb");
        }finally {
            System.out.println("finally");
        }
    }


}
