package test.com.springboot.autoconfig.grpc.server;

import com.google.common.collect.Lists;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;

import java.util.Iterator;
import java.util.List;

public class NettyGrpcServerFactory {

    private final GrpcServerProperties properties;
    private final List<GrpcServiceDefinition> services = Lists.newLinkedList();

    public NettyGrpcServerFactory(GrpcServerProperties properties){
        this.properties = properties;
    }

    public Server createServer() {
        NettyServerBuilder builder = NettyServerBuilder.forPort(this.getPort());
        Iterator var2 = this.services.iterator();
        while(var2.hasNext()) {
            GrpcServiceDefinition service = (GrpcServiceDefinition)var2.next();
            builder.addService(service.getDefinition());
        }

        return builder.build();
    }


    public String getAddress() {
        return this.properties.getAddress();
    }

    public int getPort() {
        return this.properties.getPort();
    }

    public void addService(GrpcServiceDefinition service) {
        services.add(service);
    }
}
