package com.annotation.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * BeanFactoryPostProcessor BeanFactory的后置处理器
 * BeanFactory初始化之后，所有的Bean保存加载了，没有创建Bean实例
 * <p>
 * invokeBeanFactoryPostProcessors(beanFactory);
 * 先从beanFactory找到BeanFactoryPostProcessor的组件
 * String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
 * 循环找到没有包含这两个PriorityOrdered.class和Ordered.class接口类放到nonOrderedPostProcessorNames队列里面
 * for (String ppName : postProcessorNames) {
 * }
 * 在循环nonOrderedPostProcessorNames这个队列放到nonOrderedPostProcessors队列里面
 * List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
 * for (String postProcessorName : nonOrderedPostProcessorNames) {
 * nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
 * }
 * 执行nonOrderedPostProcessors队列里面BeanFactoryPostProcessor循环执行回调
 * invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);
 */

@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("MyBeanFactoryPostProcessor......postProcessBeanFactory");
        int count = beanFactory.getBeanDefinitionCount();
        String[] names = beanFactory.getBeanDefinitionNames();
        System.out.println("有多少个bean--->" + count);
        System.out.println(Arrays.toString(names));
    }
}
