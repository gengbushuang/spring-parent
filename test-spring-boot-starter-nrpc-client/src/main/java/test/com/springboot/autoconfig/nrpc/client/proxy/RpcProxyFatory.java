package test.com.springboot.autoconfig.nrpc.client.proxy;

import io.netty.channel.Channel;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;

public interface RpcProxyFatory {

	public <T> RpcProxy<T> getProxy(Class<T> protocol, Channel channel,InvocationHandler invocationHandler) throws IOException;
}
