package test.com.springboot.autoconfig.grpc.client.channel.discovery;

import io.grpc.Channel;
import io.grpc.LoadBalancer;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.channel.ChannelOption;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import test.com.springboot.autoconfig.grpc.client.GrpcChannelProperties;
import test.com.springboot.autoconfig.grpc.client.GrpcChannelsProperties;
import test.com.springboot.autoconfig.grpc.client.channel.GrpcChannelFactory;

import java.util.concurrent.TimeUnit;

public class DiscoveryClientChannelFactory implements GrpcChannelFactory {

    private GrpcChannelsProperties properties;
    private DiscoveryClient client;
    private LoadBalancer.Factory loadBalancerFactory;

    public DiscoveryClientChannelFactory(GrpcChannelsProperties properties, DiscoveryClient client, LoadBalancer.Factory loadBalancerFactory) {
        this.properties = properties;
        this.client = client;
        this.loadBalancerFactory = loadBalancerFactory;
    }


    @Override
    public Channel createChannel(String name, int connTimeoutMillis) {
        GrpcChannelProperties channelProperties = this.properties.getChannel(name);
        NettyChannelBuilder builder = (NettyChannelBuilder) NettyChannelBuilder.forTarget(name)
                .loadBalancerFactory(this.loadBalancerFactory)
                .nameResolverFactory(new DiscoveryClientResolverFactory(this.client))
                .usePlaintext(this.properties.getChannel(name).isPlaintext())
                .withOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, connTimeoutMillis <= 0 ? 2000 : connTimeoutMillis);

        if (channelProperties.isEnableKeepAlive()) {
            builder.keepAliveWithoutCalls(channelProperties.isKeepAliveWithoutCalls())
                    .keepAliveTime(channelProperties.getKeepAliveTime(), TimeUnit.SECONDS)
                    .keepAliveTimeout(channelProperties.getKeepAliveTimeout(), TimeUnit.SECONDS);

        }
        Channel channel = builder.build();
        return channel;
    }
}
