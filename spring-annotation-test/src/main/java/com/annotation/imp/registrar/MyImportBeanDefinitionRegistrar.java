package com.annotation.imp.registrar;

import com.annotation.model.RedBlue;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    //AnnotationMetadata 当前类的注解
    //BeanDefinitionRegistry 注册类 把需要bean添加的容器里面registerBeanDefinition方法
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        boolean b1 = registry.containsBeanDefinition("com.annotation.model.Blue");
        boolean b2 = registry.containsBeanDefinition("com.annotation.model.Red");
        if (b1 && b2) {
            RootBeanDefinition rootDef = new RootBeanDefinition(RedBlue.class);
            //手动注册bean
            //bean名称
            //bean的Definition描述
            registry.registerBeanDefinition("red_blue", rootDef);
        }
    }
}
