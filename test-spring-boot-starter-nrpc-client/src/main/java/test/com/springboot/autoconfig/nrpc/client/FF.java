package test.com.springboot.autoconfig.nrpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import test.com.springboot.autoconfig.nrpc.client.handler.RpcHandler;
import test.com.springboot.autoconfig.nrpc.client.handler.codec.RpcDecoder;
import test.com.springboot.autoconfig.nrpc.client.handler.codec.RpcEncoder;
import test.com.springboot.autoconfig.nrpc.client.model.RpcRequest;
import test.com.springboot.autoconfig.nrpc.client.model.RpcResponse;
import test.com.springboot.autoconfig.nrpc.client.proxy.DefautRpcProxyFatory;
import test.com.springboot.autoconfig.nrpc.client.proxy.RpcProxy;

import javax.net.SocketFactory;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class FF {
    public static void main(String[] args) throws IOException {
        RpcHandler rpcHandler = new RpcHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new RpcEncoder(RpcRequest.class))
                                .addLast(new RpcDecoder(RpcResponse.class))
                                .addLast(rpcHandler);
                    }
                });

        ChannelFuture future = bootstrap.connect("127.0.0.1", 9090);
        try {
            future.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DefautRpcProxyFatory proxyFatory = new DefautRpcProxyFatory();
        RpcProxy<HelloService> rpcProxy = proxyFatory.getProxy(HelloService.class, future.channel(), rpcHandler);
        HelloService proxy = rpcProxy.getProxy();
        for(int i = 0;i<1;i++) {
            System.out.println(i);
            String name = proxy.hello("我梦");
            System.out.println(name);
        }

    }
}