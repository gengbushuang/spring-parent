package test.com.springboot.autoconfig.nrpc.client;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import test.com.springboot.autoconfig.nrpc.client.handler.RpcHandler;
import test.com.springboot.autoconfig.nrpc.client.proxy.RpcProxy;
import test.com.springboot.autoconfig.nrpc.client.proxy.RpcProxyFatory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class NrpcClientBeanPostProcessor implements BeanPostProcessor {

    private Map<String, List<Class>> beansToProcess = new HashMap();

    @Autowired
    private RpcProxyFatory rpcProxyFatory;

    @Autowired
    private RpcHandler rpcHandler;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        while (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            int length = declaredFields.length;
            for (int i = 0; i < length; ++i) {
                Field field = declaredFields[i];
                //判断标有GrpcClient注解字段
                if (field.isAnnotationPresent(NrpcClient.class)) {
                    //建立beanName--class
                    // map对应关系
                    if (!this.beansToProcess.containsKey(beanName)) {
                        this.beansToProcess.put(beanName, new ArrayList<>());
                    }
                    this.beansToProcess.get(beanName).add(clazz);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (this.beansToProcess.containsKey(beanName)) {
            //
            Object targetBean = this.getTargetBean(bean);
            Iterator<Class> iterator = this.beansToProcess.get(beanName).iterator();
            while (iterator.hasNext()) {
                Class aClass = iterator.next();
                Field[] declaredFields = aClass.getDeclaredFields();
                int length = declaredFields.length;
                for (int i = 0; i < length; ++i) {
                    Field field = declaredFields[i];
                    //判断标有GrpcClient注解字段
                    if (field.isAnnotationPresent(NrpcClient.class)) {
                        try {
                            RpcProxy<?> rpcProxy = rpcProxyFatory.getProxy(field.getType(), null, rpcHandler);
                            Object proxy = rpcProxy.getProxy();
                            ReflectionUtils.makeAccessible(field);
                            //NrpcClient注解字段初始化
                            ReflectionUtils.setField(field, targetBean, proxy);
                        } catch (IOException e) {
                            throw new BeanCreationException("nrpc初始化bean异常",e);
                        }

                    }
                }
            }
        }
        return bean;
    }

    /**
     * 判断是否APO代理类
     * 找到真正的对象
     *
     * @param bean
     * @return
     * @throws Exception
     */
    private Object getTargetBean(Object bean) {
        try {
            Object target = bean;
            while (AopUtils.isAopProxy(target)) {
                target = ((Advised) target).getTargetSource().getTarget();
            }
            return target;
        } catch (Throwable throwable) {
            throw new RuntimeException("is not getTargetBean " + bean, throwable);
        }
    }
}
