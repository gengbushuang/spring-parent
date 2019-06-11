package test.com.springboot.autoconfig.nrpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.com.springboot.autoconfig.nrpc.server.handler.RpcHandler;
import test.com.springboot.autoconfig.nrpc.server.handler.codec.RpcDecoder;
import test.com.springboot.autoconfig.nrpc.server.handler.codec.RpcEncoder;
import test.com.springboot.autoconfig.nrpc.server.model.RpcRequest;
import test.com.springboot.autoconfig.nrpc.server.model.RpcResponse;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Map;

public class NrpcService {

    private static final Logger log = LoggerFactory.getLogger(NrpcService.class);

    private volatile boolean shutdown;

    private final SocketAddress address;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Channel channel;

    private Map<String, Object> handlerMap;


    public NrpcService(SocketAddress address, EventLoopGroup bossGroup, EventLoopGroup workerGroup, Map<String, Object> handlerMap) {
        this.address = address;
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;

        this.handlerMap = handlerMap;
    }

    public void start() throws IOException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        synchronized (NrpcService.this) {
                            if (channel != null && !channel.isOpen()) {
                                // Server already shutdown.
                                channel.close();
                                shutdown = true;
                                return;
                            }
                        }
                        channel.pipeline()
                                .addLast(new RpcDecoder(RpcRequest.class)) // 将 RPC 请求进行解码（为了处理请求）
                                .addLast(new RpcEncoder(RpcResponse.class)) // 将 RPC 响应进行编码（为了返回响应）
                                .addLast(new RpcHandler(handlerMap)); // 处理 RPC 请求
                    }
                });

        ChannelFuture future = bootstrap.bind(address);
        try {
            future.await();
        } catch (InterruptedException ex) {
            shutdown = true;
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted waiting for bind");
        }
        if (!future.isSuccess()) {
            shutdown = true;
            throw new IOException("Failed to bind", future.cause());
        }
        channel = future.channel();
        log.info("server started on address {}", address);
        shutdown = false;
    }

    public boolean shutdown() {
        if (channel == null || !channel.isOpen()) {
            shutdown = true;
            return shutdown;
        }

        channel.close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    log.warn("Error shutting down server", future.cause());
                }
                synchronized (NrpcService.this) {
                    shutdown = true;
                }
            }
        });
        return shutdown;
    }

    public boolean isShutdown() {
        return shutdown;
    }
}
