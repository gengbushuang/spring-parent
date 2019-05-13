package test.com.springboot.autoconfig.grpc.client;

import io.grpc.LoadBalancer;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.com.springboot.autoconfig.grpc.client.channel.GrpcChannelFactory;
import test.com.springboot.autoconfig.grpc.client.channel.discovery.DiscoveryClientChannelFactory;

@Configuration
@EnableConfigurationProperties
//判断当前classpath下是否存在指定GrpcChannelFactory类，若有就自动装配
@ConditionalOnClass({GrpcChannelFactory.class})
public class GrpcClientAutoConfiguration {

    //当前上下文bean里面不存在这个GrpcChannelsProperties，自动装载
    @ConditionalOnMissingBean
    @Bean
    public GrpcChannelsProperties grpcChannelsProperties() {
        return new GrpcChannelsProperties();
    }

    @Bean
    //判断当前classpath下是否存在指定GrpcClient类，若有就自动装配
    @ConditionalOnClass({GrpcClient.class})
    public GrpcClientBeanPostProcessor grpcClientBeanPostProcessor() {
        return new GrpcClientBeanPostProcessor();
    }

    //当前上下文bean里面不存在这个LoadBalancer.Factory，自动装载
    @ConditionalOnMissingBean
    @Bean
    public LoadBalancer.Factory grpcLoadBalancerFactory() {
        return RoundRobinLoadBalancerFactory.getInstance();
    }

    @Configuration
    //判断当前classpath下是否存在指定DiscoveryClient类，若有就自动装配
    @ConditionalOnClass({DiscoveryClient.class})
    protected static class DiscoveryGrpcClientAutoConfiguration {
        protected DiscoveryGrpcClientAutoConfiguration() {
        }

        //当前上下文bean里面不存在这个GrpcChannelFactory，自动装载
        @ConditionalOnMissingBean
        @Bean
        public GrpcChannelFactory discoveryClientChannelFactory(GrpcChannelsProperties properties, DiscoveryClient client, LoadBalancer.Factory loadBalancerFactory){
            return new DiscoveryClientChannelFactory(properties,client,loadBalancerFactory);
        }
    }

}
