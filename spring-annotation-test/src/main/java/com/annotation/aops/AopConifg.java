package com.annotation.aops;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * EnableAspectJAutoProxy
 * AspectJAutoProxyRegistrar
 * AnnotationAwareAspectJAutoProxyCreator
 *  ->AspectJAwareAdvisorAutoProxyCreator
 *    ->AbstractAdvisorAutoProxyCreator
 *      ->AbstractAutoProxyCreator
 *       -->继承接口implements SmartInstantiationAwareBeanPostProcessor(
 *          InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation方法
 *          BeanPostProcessor.postProcessBeforeInitialization方法
 *          BeanPostProcessor.postProcessAfterInitialization方法
 *       )在bean初始化完成前后做事情, BeanFactoryAware(
 *          BeanFactoryAware.setBeanFactory方法
 *       )自动装配BeanFactory
 *
 * EnableAspectJAutoProxy得过程
 * 1.@Import(AspectJAutoProxyRegistrar.class)：给容器中导入AspectJAutoProxyRegistrar
 * 2.利用AspectJAutoProxyRegistrar容器注册bean：BeanDefinition
 * 3.AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);
 * 4.BeanDefinitionRegistry容器注册-->registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);
 * 5.给容器中注入org.springframework.aop.config.internalAutoProxyCreator=org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
 *
 *
 * 流程
 * 1.
 * 2.refresh();刷新容器
 * 3.registerBeanPostProcessors(beanFactory);注册bean的后置处理器进行拦截
 * ->1.PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
 * ->2.String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);获取在容器里面BeanPostProcessor.class接口类进行创建
 * ->3.优先实现PriorityOrdered.class接口的BeanPostProcessor-->beanFactory.getBean(ppName, BeanPostProcessor.class);
 * ->4.装载要实现Ordered.class接口的BeanPostProcessor-->List<String> orderedPostProcessorNames = new ArrayList<>()
 * ->5.装载没实现优先级接口的BeanPostProcessor-->List<String> nonOrderedPostProcessorNames = new ArrayList<>()
 * ->6.在实现Ordered.class接口的BeanPostProcessor-->beanFactory.getBean(ppName, BeanPostProcessor.class);
 * ->7.在实现没有优先级接口的BeanPostProcessor-->beanFactory.getBean(ppName, BeanPostProcessor.class);
 * ->创建internalAutoProxyCreator的BeanPostProcessor【AnnotationAwareAspectJAutoProxyCreator】
 *      ->1.beanFactory.getBean(ppName, BeanPostProcessor.class);
 *      ->2.beanFactory.doGetBean(name, requiredType, null, false);
 *      ->3.createBean(beanName, mbd, args);创建实例
 *      ->4.populateBean(beanName, mbd, instanceWrapper);给bean的各种属性赋值
 *      ->5.Object beanInstance = doCreateBean(beanName, mbdToUse, args);
 *      ->6.initializeBean(beanName, exposedObject, mbd);初始化bean
 *          ->1.invokeAwareMethods(beanName, bean);处理Aware接口的方法回调
 *          ->2.applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);处理BeanPostProcessor.postProcessBeforeInitialization的方法
 *          ->3.invokeInitMethods(beanName, wrappedBean, mbd);处理afterPropertiesSet()的方法
 *          ->4.applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);处理BeanPostProcessor.postProcessAfterInitialization的方法
 *      ->7.AbstractAdvisorAutoProxyCreator.setBeanFactory方法--》自动装配BeanFactory
 *      ->8.AnnotationAwareAspectJAutoProxyCreator.initBeanFactory方法
 *          在这个方法里面创建了AspectJAdvisorFactory和BeanFactoryAspectJAdvisorsBuilderAdapter
 *
 * 4.finishBeanFactoryInitialization(beanFactory);完成BeanFactory初始化工作；创建剩下的单实例bean
 * ->1.beanFactory.preInstantiateSingletons();
 *      1.List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);
 *          遍历获取所有bean，getBean(beanName);创建bean
 *          getBean->doGetBean()->getSingleton()->
 *      2.创建bean
 *          先从缓存中获取当前bean，如果能获取到，说明bean是之前被创建过的，直接使用，否则再创建；
 *              Object sharedInstance = getSingleton(beanName);这个就是第一次去从缓存获取
 *          createBean(beanName, mbd, args);
 *          Object bean = resolveBeforeInstantiation(beanName, mbdToUse);解析BeanPostProcessor，能够返回一个return a proxy
 *              bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
 *              解析InstantiationAwareBeanPostProcessor--》在任何bean创建之前调用，而BeanPostProcessor是在Bean对象创建完成初始化前后调用的
 *              执行postProcessBeforeInstantiation方法
 *              这个地方就会调用AbstractAutoProxyCreator.postProcessBeforeInstantiation方法
 *          Object beanInstance = doCreateBean(beanName, mbdToUse, args);上面return a proxy为null，就真正的去创建一个bean实例，跟上面6.initializeBean。。的步骤一样
 *
 * 5.AbstractAutoProxyCreator.postProcessBeforeInstantiation方法作用
 * ->1.Object cacheKey = getCacheKey(beanClass, beanName);
 * ->2.if (this.advisedBeans.containsKey(cacheKey))判断advisedBeans是否存在
 * ->3.if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
 *      isInfrastructureClass(beanClass)-->判断这个bean是否基础类型Advice.class，Pointcut.class，Advisor.class，AopInfrastructureBean.class
 *      shouldSkip(beanClass, beanName)-->
 * 6.AbstractAutoProxyCreator.postProcessAfterInitialization方法作用
 * ->1.return wrapIfNecessary(bean, beanName, cacheKey);
 *     ->1.Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);获取当前bean的所有增强器（通知方法）
 *         ->1.List<Advisor> candidateAdvisors = findCandidateAdvisors();获取当前bean的所有增强器
 *         ->2.List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);找到候选的所有的增强器（找哪些通知方法是需要切入当前bean方法的）
 *         ->3.extendAdvisors(eligibleAdvisors);主要判断当前Advisor类型PointcutAdvisor，增加方法拦截ExposeInvocationInterceptor.ADVISOR
 *         ->4.sortAdvisors(eligibleAdvisors);给Advisor排序
 *     ->2.this.advisedBeans.put(cacheKey, Boolean.TRUE);保存当前bean在advisedBeans中；
 *     ->3.Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));如果当前bean需要增强，创建当前bean的代理对象；
 *         ->1.Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);获取所有增强器（通知方法）
 *         ->2.proxyFactory.addAdvisors(advisors);保存到代理工厂里面
 *         ->3.proxyFactory.getProxy(getProxyClassLoader());代理工厂创建代理
 *         ->4.getAopProxyFactory().createAopProxy(this);创建代理对象：Spring自动决定
 *              JdkDynamicAopProxy(config);jdk动态代理；
 *  			ObjenesisCglibAopProxy(config);cglib的动态代理；
 *     ->4.this.proxyTypes.put(cacheKey, proxy.getClass());保存动态代理类
 * ->2.以后容器中获取到的就是这个组件的代理对象，执行目标方法的时候，代理对象就会执行通知方法的流程
 *
 * 代理流程
 * 1.CglibAopProxy.intercept-》TestAop调用方法之前进入这个代理方法
 * 2.TargetSource targetSource = this.advised.getTargetSource();-》找到目标类TestAop
 * 3.List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);创建这个method方法的调用链
 *      1.List<Object> cached = this.methodCache.get(cacheKey);查找这个方法创建的链是否在缓存中
 *      2.如果不存在就进行创建调用拦截器
 *          this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(this, method, targetClass);
 *          1.Advisor[] advisors = config.getAdvisors();获取所有Advisor的增强方法(通知器)
 *          2.List<Object> interceptorList = new ArrayList<>(advisors.length);这个是保存的拦截器链
 *          3.循环所有Advisor集合，将其转为Interceptor加入
 *              registry.getInterceptors(advisor)转换
 *              1.List<MethodInterceptor> interceptors = new ArrayList<>(3);MethodInterceptor的拦截器集合
 *              2.如果advice instanceof MethodInterceptor就直接加入到集合中interceptors.add((MethodInterceptor) advice);
 *              3.如果是advice instanceof AfterReturningAdvice，就利用new AfterReturningAdviceInterceptor(advice);转换MethodInterceptor放到集合中
 *              4.如果是advice instanceof MethodBeforeAdvice，就利用new MethodBeforeAdviceInterceptor(advice);转换MethodInterceptor放到集合中
 *      3.this.methodCache.put(cacheKey, cached);创建完成，保存到缓存中
 *  4.retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();创建CglibMethodInvocation开始进行调用
 *      1.这个地方是如果当前下标等于这个数组长度就直接调用invokeJoinpoint();
 *          if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
 * 			    return invokeJoinpoint();
 *          }
 *      2.Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
 *          获取数组链的拦截器链类，对currentInterceptorIndex下标进行递增
 *      3.((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);调用链接器链invoke方法、
 *      4.然而链接器链invoke方法又继续调用CglibMethodInvocation的proceed()方法，进行循环获取this.interceptorsAndDynamicMethodMatchers的拦截器。
 *      5.效果：
 *          正常执行：前置通知-》目标方法-》后置通知-》返回通知
 *  * 		出现异常：前置通知-》目标方法-》后置通知-》异常通知
 *
 *
 */


@EnableAspectJAutoProxy
@Configuration
public class AopConifg {

    //业务逻辑类加入容器中
    @Bean
    public TestAop testAop() {
        return new TestAop();
    }

    //切面类加入到容器中
    @Bean
    public LogAspects logAspects() {
        return new LogAspects();
    }
}
