package test.com.springboot.autoconfig.grpc.server;

import io.grpc.Channel;
import io.grpc.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Iterator;

@Configuration
@EnableConfigurationProperties(GrpcServerProperties.class)
@ConditionalOnClass({Server.class, NettyGrpcServerFactory.class})
public class GrpcServerAutoConfiguration {

    public GrpcServerAutoConfiguration() {
    }

//    @ConditionalOnMissingBean
    @ConditionalOnBean(annotation = GrpcService.class)
    @Bean
    public AnnotationGrpcServiceDiscoverer defaultGrpcServiceFinder() {
        return new AnnotationGrpcServiceDiscoverer();
    }


    //    @ConditionalOnMissingBean
    @ConditionalOnClass({Channel.class})
    @ConditionalOnBean(value = AnnotationGrpcServiceDiscoverer.class)
    @Bean
    public NettyGrpcServerFactory nettyGrpcServiceFactory(GrpcServerProperties properties, AnnotationGrpcServiceDiscoverer discoverer) {
        NettyGrpcServerFactory factory = new NettyGrpcServerFactory(properties);
        Iterator<GrpcServiceDefinition> var4 = discoverer.findGrpcServices().iterator();
        while (var4.hasNext()) {
            GrpcServiceDefinition service = (GrpcServiceDefinition) var4.next();
            factory.addService(service);
        }

        return factory;

    }

//    @ConditionalOnMissingBean
    @ConditionalOnBean(value = NettyGrpcServerFactory.class)
    @Bean
    public GrpcServerLifecycle grpcServerLifecycle(NettyGrpcServerFactory factory) {
        return new GrpcServerLifecycle(factory);
    }

}
