package com.rpc;

import com.actoconfigure.test.model.rpc_package.HelloReply;
import com.actoconfigure.test.model.rpc_package.HelloRequest;
import com.actoconfigure.test.model.rpc_package.HelloWorldServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class RpcService {

    private Server server;

    private void start() throws IOException {
        int port = 8888;
        server = ServerBuilder.forPort(port)
                .addService(new HelloWorldImpl())
                .build().start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                RpcService.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }


    static class HelloWorldImpl extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {
        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final RpcService server1 = new RpcService();
        server1.start();
        server1.blockUntilShutdown();
    }
}
