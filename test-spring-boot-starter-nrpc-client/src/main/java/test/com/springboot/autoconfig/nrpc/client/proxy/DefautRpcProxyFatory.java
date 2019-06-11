package test.com.springboot.autoconfig.nrpc.client.proxy;


import io.netty.channel.Channel;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DefautRpcProxyFatory implements RpcProxyFatory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> RpcProxy<T> getProxy(Class<T> protocol, Channel channel, InvocationHandler invocationHandler) throws IOException {
//        Invoker invoker = new Invoker(protocol,channel);
        T t = (T) Proxy.newProxyInstance(protocol.getClassLoader(), new Class[]{protocol}, invocationHandler);
        return new RpcProxy<T>(protocol, t);
    }

}
