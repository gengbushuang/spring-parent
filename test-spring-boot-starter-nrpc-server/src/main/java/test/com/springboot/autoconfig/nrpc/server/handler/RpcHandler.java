package test.com.springboot.autoconfig.nrpc.server.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import test.com.springboot.autoconfig.nrpc.server.model.RpcRequest;
import test.com.springboot.autoconfig.nrpc.server.model.RpcResponse;
import test.com.springboot.autoconfig.nrpc.server.exception.NrpcException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private Map<String, Object> rpcMap;

    public RpcHandler(Map<String, Object> rpcMap) {
        this.rpcMap = rpcMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handler(request);
//            System.out.println("channelRead0---------------->" + result);
            response.setVersion(request.getVersion());
            response.setResult(result);
        } catch (Throwable e) {
            response.setError(e);
        }
        ctx.writeAndFlush(response);
//        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handler(RpcRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //类名
        String className = request.getClassName();
        if (!rpcMap.containsKey(className)) {
            throw new NrpcException("找不到对应的类:" + className);
        }
        //方法名
        String methodName = request.getMethodName();
        //方法的参数类型
        Class<?>[] parameterClasses = request.getParameterClasses();
        //方法参数
        Object[] parameters = request.getParameters();
        Object o = rpcMap.get(className);
        //获取方法对象
        Method method = o.getClass().getMethod(methodName, parameterClasses);
        //调用类对应的方法
        Object invoke = method.invoke(o, parameters);

        return invoke;
    }
}
