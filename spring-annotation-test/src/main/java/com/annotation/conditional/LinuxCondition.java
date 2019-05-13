package com.annotation.conditional;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LinuxCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //当前环境的信息
        Environment environment = context.getEnvironment();
        //bean定义的注册
        BeanDefinitionRegistry registry = context.getRegistry();
        //类加载器
        ClassLoader classLoader = context.getClassLoader();
        //ioc使用的beanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        String property = environment.getProperty("os.name");
        return property.contains("Linux");
    }
}
