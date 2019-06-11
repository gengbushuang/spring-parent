package test.com.springboot.autoconfig.nrpc.server;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

public class NrpcServiceBuilder {

    private final SocketAddress address;

    private Map<String, Object> handlerMap;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;


    private NrpcServiceBuilder(SocketAddress address) {
        this.address = address;
    }

    public static NrpcServiceBuilder forAddress(SocketAddress address) {
        return new NrpcServiceBuilder(address);
    }


    public NrpcServiceBuilder bossEventLoopGroup(EventLoopGroup group) {
        this.bossGroup = group;
        return this;
    }

    public NrpcServiceBuilder workerEventLoopGroup(EventLoopGroup group) {
        this.workerGroup = group;
        return this;
    }

    public NrpcServiceBuilder putHandlerMap(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
        return this;
    }

    public NrpcService build() {
        return new NrpcService(address,
                bossGroup,
                workerGroup,
                handlerMap);
    }
}
