package test.com.springboot.autoconfig.grpc.client.channel;

import io.grpc.Channel;

public interface GrpcChannelFactory {
    Channel createChannel(String channelName, int timeout);
}
