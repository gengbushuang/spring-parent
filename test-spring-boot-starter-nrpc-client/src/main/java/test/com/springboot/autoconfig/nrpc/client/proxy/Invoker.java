package test.com.springboot.autoconfig.nrpc.client.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import test.com.springboot.autoconfig.nrpc.client.concurrent.RpcFuture;
import test.com.springboot.autoconfig.nrpc.client.model.RpcRequest;
import test.com.springboot.autoconfig.nrpc.client.model.RpcResponse;
import test.com.springboot.autoconfig.nrpc.client.serialize.SerializationUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

public class Invoker implements RpcInvocationHandler {

    private final Class<?> protocol;
    private Channel channel;

    public Invoker(Class<?> _protocol, Channel channel) {
        this.protocol = _protocol;
        this.channel = channel;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
        request.setVersion(1234);
        request.setClassName("com.nrpc.test.HelloService");
        request.setMethodName(method.getName());
        request.setParameterClasses(method.getParameterTypes());
        request.setParameters(args);

        RpcFuture rpcFuture = new RpcFuture(request);
        final CountDownLatch latch = new CountDownLatch(1);

        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                latch.countDown();
            }
        });

        return null;
    }
}