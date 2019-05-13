package com.annotation.beanlife;

import com.annotation.model.Life;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.annotation.beanlife")
@Import({LifeImpl.class,LifeAnnotation.class})
public class BeanLifeConfig {
    //单例模式 容器启动的时候就创建bean并且执行initMethod方法，容器关闭的时候执行destroyMethod方法
    //多例模式 获取bean的时候才创建bean并且执行initMethod方法，容器关闭的时候不执行destroyMethod方法，自己处理

    //可以实现InitializingBean接口进行初始化和DisposableBean接口进行关闭

    //可以实现@PostConstruct注解进行初始化
    //可以实现@PreDestroy注解进行销毁

    //BeanPostProcessor接口，bean的后置处理器
    //  postProcessBeforeInitialization在初始化方法之前执行
    //  postProcessAfterInitialization在初始化方法之后执行
    @Bean(initMethod="init",destroyMethod = "destroy")
    @Scope("prototype")
    public Life life(){
        return new Life();
    }
}
