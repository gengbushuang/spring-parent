package com.actoconfigure.test.test;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import test.com.springboot.autoconfig.grpc.client.GrpcClient;

import java.lang.reflect.Field;
import java.util.*;

//@Component
public class ClientHandler implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Map<String, Set<Class>> beansToProcess = new HashMap();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
//        System.out.println("clazz---->" + clazz + ",beanName----->" + beanName);
        do {
            Field[] declaredFields = clazz.getDeclaredFields();

            int length = declaredFields.length;

            for (int i = 0; i < length; ++i) {
                Field field = declaredFields[i];
//                System.out.println("field---->" + field);
                if (field.isAnnotationPresent(GrpcClient.class)) {
                    System.out.println("---------------------------------------------------------->" + beanName);
                    System.out.println("---------------------------------------------------------->" + clazz);
                    if (!this.beansToProcess.containsKey(beanName)) {
                        this.beansToProcess.put(beanName, new HashSet<>());
                    }
                    this.beansToProcess.get(beanName).add(clazz);
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (this.beansToProcess.containsKey(beanName)) {
//            Object targetBean = this.getTargetBean(bean);
            System.out.println("------------------------------------------------------------------------->");
            System.out.println("bean-------------------------->" + bean);
            Iterator<Class> iterator = this.beansToProcess.get(beanName).iterator();
            while (iterator.hasNext()) {
                Class aClass = iterator.next();
                System.out.println("aClass-------------------------->" + aClass);
                Field[] declaredFields = aClass.getDeclaredFields();
                int length = declaredFields.length;
                for (int i = 0; i < length; ++i) {
                    Field field = declaredFields[i];
                    System.out.println("field-------------------------->" + field);
                    GrpcClient grpcClient = AnnotationUtils.getAnnotation(field, GrpcClient.class);
                    if (null != grpcClient) {
                        System.out.println("grpcClient-------------------------->" + grpcClient);
//                        ReflectionUtils.makeAccessible(field);
//                        ReflectionUtils.setField(field,targetBean,null);
                    }
                }

            }
        }
        return bean;
    }

    /**
     * 判断是否APO代理类
     * 找到真正的对象
     * @param bean
     * @return
     * @throws Exception
     */
    private Object getTargetBean(Object bean) throws Exception {
        try {
            Object target = bean;
            while (AopUtils.isAopProxy(target)) {
                target = ((Advised) target).getTargetSource().getTarget();
            }
            return target;
        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
