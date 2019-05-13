package test.com.springboot.autoconfig.grpc.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "grpc")
public class GrpcChannelsProperties {

    @NestedConfigurationProperty
    private Map<String, GrpcChannelProperties> client = new HashMap();

    public GrpcChannelsProperties() {
    }

    public GrpcChannelProperties getChannel(String name) {
        GrpcChannelProperties grpcChannelProperties = (GrpcChannelProperties)this.client.get(name);
        if (grpcChannelProperties == null) {
            grpcChannelProperties = GrpcChannelProperties.DEFAULT;
        }

        return grpcChannelProperties;
    }

    public Map<String, GrpcChannelProperties> getClient() {
        return client;
    }

    public void setClient(Map<String, GrpcChannelProperties> client) {
        this.client = client;
    }
}
