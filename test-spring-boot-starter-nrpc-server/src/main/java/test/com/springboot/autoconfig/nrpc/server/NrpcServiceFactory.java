package test.com.springboot.autoconfig.nrpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.com.springboot.autoconfig.nrpc.server.handler.RpcHandler;
import test.com.springboot.autoconfig.nrpc.server.handler.codec.RpcDecoder;
import test.com.springboot.autoconfig.nrpc.server.handler.codec.RpcEncoder;

import java.net.InetSocketAddress;
import java.util.Map;

public class NrpcServiceFactory {

    private static final Logger log = LoggerFactory.getLogger(NrpcServiceFactory.class);

    private NrpcServiceProperties properties;

    private Map<String, Object> handlerMap;

    public NrpcServiceFactory(NrpcServiceProperties properties, Map<String, Object> handlerMap) {
        this.properties = properties;
        this.handlerMap = handlerMap;
    }


    public NrpcService create() throws InterruptedException {
        return NrpcServiceBuilder.forAddress(new InetSocketAddress(properties.getAddress(), properties.getPort()))
                .bossEventLoopGroup(new NioEventLoopGroup())
                .workerEventLoopGroup(new NioEventLoopGroup())
                .putHandlerMap(handlerMap).build();
    }
}
