package test.com.springboot.autoconfig.nrpc.server;

import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Iterator;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(NrpcServiceProperties.class)
@ConditionalOnClass({NioServerSocketChannel.class, NrpcServiceFactory.class})
public class NrpcServerAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public AnnotationNrpcService defaultNrpcServiceFinder() {
        return new AnnotationNrpcService();
    }

    @ConditionalOnMissingBean
    @Bean
    public NrpcServiceFactory nettyGrpcServiceFactory(NrpcServiceProperties properties, AnnotationNrpcService annotationNrpcService) {
        Map<String, Object> servers = annotationNrpcService.findNrpcServers();
        NrpcServiceFactory factory = new NrpcServiceFactory(properties, servers);
        return factory;
    }

    @ConditionalOnMissingBean
    @Bean
    public ServerLifecycle grpcServerLifecycle(NrpcServiceFactory factory) {
        return new ServerLifecycle(factory);
    }


}
