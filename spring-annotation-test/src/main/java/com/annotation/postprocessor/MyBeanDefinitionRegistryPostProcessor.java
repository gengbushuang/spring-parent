package com.annotation.postprocessor;

import com.annotation.model.Life;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * BeanDefinitionRegistryPostProcessor继承BeanFactoryPostProcessor
 * 优先于BeanFactoryPostProcessor执行
 *
 * invokeBeanFactoryPostProcessors(beanFactory);
 *从beanFactory找到BeanDefinitionRegistryPostProcessor的组件
 * String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
 * 循环
 * for (String ppName : postProcessorNames) {
 * 这里面判断不包含有PriorityOrdered.class和Ordered.class的BeanDefinitionRegistryPostProcessor组件
 * 执行回调BeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry方法
 *
 * }
 * 执行执行回调BeanDefinitionRegistryPostProcessor.postProcessBeanFactory方法
 * invokeBeanFactoryPostProcessors(registryPostProcessors, beanFactory);
 *
 */

@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    //BeanDefinitionRegistry Bean的定义保存信息
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("MyBeanDefinitionRegistryPostProcessor......postProcessBeanDefinitionRegistry bean数量"+registry.getBeanDefinitionCount());
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Life.class);
        registry.registerBeanDefinition("hello",rootBeanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("MyBeanDefinitionRegistryPostProcessor......postProcessBeanFactory bean数量"+beanFactory.getBeanDefinitionCount());
    }
}
