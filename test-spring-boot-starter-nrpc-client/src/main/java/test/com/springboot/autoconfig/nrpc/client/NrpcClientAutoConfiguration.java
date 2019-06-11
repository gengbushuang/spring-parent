package test.com.springboot.autoconfig.nrpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import test.com.springboot.autoconfig.nrpc.client.handler.RpcHandler;
import test.com.springboot.autoconfig.nrpc.client.handler.codec.RpcDecoder;
import test.com.springboot.autoconfig.nrpc.client.handler.codec.RpcEncoder;
import test.com.springboot.autoconfig.nrpc.client.model.RpcRequest;
import test.com.springboot.autoconfig.nrpc.client.model.RpcResponse;
import test.com.springboot.autoconfig.nrpc.client.proxy.DefautRpcProxyFatory;
import test.com.springboot.autoconfig.nrpc.client.proxy.RpcProxyFatory;

@Configurable
@EnableConfigurationProperties(NrpcClientProperties.class)
public class NrpcClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RpcProxyFatory rpcProxyFatory() {
        return new DefautRpcProxyFatory();
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcHandler rpcHandler(NrpcClientProperties properties) {
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
        return rpcHandler;
    }

    @Bean
    //判断当前classpath下是否存在指定GrpcClient类，若有就自动装配
    @ConditionalOnClass({NrpcClient.class})
    public NrpcClientBeanPostProcessor grpcNrpcClientBeanPostProcessor() {
        return new NrpcClientBeanPostProcessor();
    }
}
