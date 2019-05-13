package test.com.springboot.autoconfig.grpc.client;

import io.grpc.Channel;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import test.com.springboot.autoconfig.grpc.client.channel.GrpcChannelFactory;

import java.lang.reflect.Field;
import java.util.*;

public class GrpcClientBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Map<String, List<Class>> beansToProcess = new HashMap();

    @Autowired
    private GrpcChannelFactory channelFactory;

    /**
     * 实例化之前进行处理
     * <p>
     * 找到有GrpcClient注解的类
     * 并把这个类保存到map里面
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        while (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            int length = declaredFields.length;
            for (int i = 0; i < length; ++i) {
                Field field = declaredFields[i];
                //判断标有GrpcClient注解字段
                if (field.isAnnotationPresent(GrpcClient.class)) {
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

    /**
     * 实例化之后进行处理
     * 对GrpcClient的字段进行实例化
     */
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
                    if (field.isAnnotationPresent(GrpcClient.class)) {
                        GrpcClient grpcClient = AnnotationUtils.getAnnotation(field, GrpcClient.class);

                        Channel channel = this.pickChannelFactory(grpcClient).createChannel(grpcClient.value(), grpcClient.timeoutMillis());
                        ReflectionUtils.makeAccessible(field);
                        //GrpcClient注解字段初始化
                        ReflectionUtils.setField(field, targetBean, channel);
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

    private GrpcChannelFactory pickChannelFactory(GrpcClient annotation) {
        return this.channelFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
