package test.com.springboot.autoconfig.grpc.client.channel.discovery;

import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import io.grpc.internal.GrpcUtil;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.List;

public class DiscoveryClientResolverFactory extends NameResolverProvider {
    private final DiscoveryClient client;

    public DiscoveryClientResolverFactory(DiscoveryClient client){
        this.client = client;
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 5;
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI targetUri, Attributes params) {
//        System.out.println("newNameResolver------->("+targetUri+","+params+")");
        return new DiscoveryClientNameResolver(targetUri.toString(), this.client, params, GrpcUtil.TIMER_SERVICE, GrpcUtil.SHARED_CHANNEL_EXECUTOR);
    }

    @Override
    public String getDefaultScheme() {
        return "discoveryClient";
    }
}
