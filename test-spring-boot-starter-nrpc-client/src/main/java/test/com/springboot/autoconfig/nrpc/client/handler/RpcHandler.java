package test.com.springboot.autoconfig.nrpc.client.handler;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.com.springboot.autoconfig.nrpc.client.concurrent.RpcFuture;
import test.com.springboot.autoconfig.nrpc.client.model.RpcRequest;
import test.com.springboot.autoconfig.nrpc.client.model.RpcResponse;
import test.com.springboot.autoconfig.nrpc.client.proxy.RpcInvocationHandler;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class RpcHandler extends SimpleChannelInboundHandler<RpcResponse> implements RpcInvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcHandler.class);

    private Map<String, RpcFuture> pending = new ConcurrentHashMap<>();

    private Map<String, Channel> channelMap = new ConcurrentHashMap<>();

//    private volatile Channel channel;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String s = channel.id().toString();
        LOGGER.info("channelRegistered-->" + s);
        channelMap.put(s, ctx.channel());
        super.channelRegistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        if (pending.containsKey(response.getRequestId())) {
            RpcFuture rpcFuture = pending.remove(response.getRequestId());
            rpcFuture.setResponse(response);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
        request.setVersion(1234);
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterClasses(method.getParameterTypes());
        request.setParameters(args);

        List<Channel> channels = new ArrayList<>(channelMap.values());
        Channel channel = channels.get(0);

        RpcFuture rpcFuture = new RpcFuture(request);
        pending.put(request.getRequestId(), rpcFuture);
        final CountDownLatch latch = new CountDownLatch(1);

        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                latch.countDown();
            }
        });
        RpcResponse response = rpcFuture.get();
        if (response.getError() != null) {
            throw response.getError();
        }
        return response.getResult();
    }
}
