package com.actoconfigure.test.test;

import com.actoconfigure.test.annotation.Table;
import com.google.common.collect.Lists;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerServiceDefinition;
import io.grpc.netty.NettyServerBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import test.com.springboot.autoconfig.grpc.server.GrpcService;
import test.com.springboot.autoconfig.grpc.server.GrpcServiceDefinition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

//@Component
public class ServiceHandler implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Collection<String> findServiceBeanNames(){
        String[] annotationNames = this.applicationContext.getBeanNamesForAnnotation(GrpcService.class);
        return Arrays.asList(annotationNames);
    }

    public Collection<GrpcServiceDefinition> findGrpcServices(){
        Collection<String> beanNames = this.findServiceBeanNames();
        List<GrpcServiceDefinition> definitions = Lists.newArrayListWithCapacity(beanNames.size());
        Iterator var5 = beanNames.iterator();
        while(var5.hasNext()) {
            String beanName = (String)var5.next();
            BindableService bindableService = (BindableService)this.applicationContext.getBean(beanName, BindableService.class);
            ServerServiceDefinition serviceDefinition = bindableService.bindService();
            GrpcService grpcServiceAnnotation = (GrpcService)this.applicationContext.findAnnotationOnBean(beanName, GrpcService.class);
            definitions.add(new GrpcServiceDefinition(beanName, bindableService.getClass(), serviceDefinition));
        }
        return definitions;
    }

    public Server crateService(){
        int port = 8888;
        NettyServerBuilder builder = NettyServerBuilder.forPort(port);
        Iterator var2 = findGrpcServices().iterator();
        while(var2.hasNext()) {
            GrpcServiceDefinition service = (GrpcServiceDefinition) var2.next();
            builder.addService(service.getDefinition());
        }
        return builder.build();
    }
}
