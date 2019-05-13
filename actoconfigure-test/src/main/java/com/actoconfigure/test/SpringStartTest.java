package com.actoconfigure.test;

import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SpringStartTest {

    private static final String[] WEB_ENVIRONMENT_CLASSES = {"javax.servlet.Servlet",
            "org.springframework.web.context.ConfigurableWebApplicationContext"};

    private boolean webEnvironment;

    private List<ApplicationContextInitializer<?>> initializers;

    public void run(Object source) {
        springDeduceWebEnvironment();
        SpringFactoriesInstancesApplicationContextInitializer();
        SpringFactoriesInstancesApplicationListener();
        SpringFactoriesInstancesSpringApplicationRunListener();
    }

    private void SpringFactoriesInstancesSpringApplicationRunListener() {
        Class<SpringApplicationRunListener> aClass = SpringApplicationRunListener.class;
        Class<?>[] types = new Class<?>[] { SpringApplication.class, String[].class };
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<String> factoryNames = SpringFactoriesLoader.loadFactoryNames(aClass, classLoader);
        Set<String> names = new LinkedHashSet<String>(factoryNames);
        names.forEach((s)->System.out.println(s));

    }

    private void SpringFactoriesInstancesApplicationListener() {
        Class<ApplicationListener> aClass = ApplicationListener.class;
        Class<?>[] parameterTypes = new Class<?>[]{};
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<String> factoryNames = SpringFactoriesLoader.loadFactoryNames(aClass, classLoader);
        Set<String> names = new LinkedHashSet<String>(factoryNames);
//        names.forEach((s)->System.out.println(s));
    }

    private void SpringFactoriesInstancesApplicationContextInitializer() {
        Class<ApplicationContextInitializer> aClass = ApplicationContextInitializer.class;
        Class<?>[] parameterTypes = new Class<?>[]{};
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<String> factoryNames = SpringFactoriesLoader.loadFactoryNames(aClass, classLoader);
        Set<String> names = new LinkedHashSet<String>(factoryNames);

        List<ApplicationContextInitializer<?>> applicationContextInitializers = SpringFactoriesInstances(aClass, parameterTypes, classLoader, names);
        AnnotationAwareOrderComparator.sort(applicationContextInitializers);

        this.initializers = new ArrayList<ApplicationContextInitializer<?>>();
        this.initializers.addAll(applicationContextInitializers);
    }

    private List<ApplicationContextInitializer<?>> SpringFactoriesInstances(Class<ApplicationContextInitializer> aClass, Class<?>[] parameterTypes, ClassLoader classLoader, Set<String> names) {
        List<ApplicationContextInitializer<?>> instances = new ArrayList<>(names.size());
        //
        for (String name : names) {
            try {
                Class<?> aClass1 = ClassUtils.forName(name, classLoader);
                Assert.isAssignable(aClass, aClass1);
                Constructor<?> constructor = aClass1.getDeclaredConstructor(parameterTypes);
                ApplicationContextInitializer o = (ApplicationContextInitializer) BeanUtils.instantiateClass(constructor,  null);
                instances.add(o);
            }catch (Throwable e){
                e.printStackTrace();
            }
        }
        return instances;
    }

    public void springDeduceWebEnvironment() {
        for (String className : WEB_ENVIRONMENT_CLASSES) {
            if (!ClassUtils.isPresent(className, null)) {
                webEnvironment = false;
            }
        }
        webEnvironment = true;
    }

    public static void main(String[] args) {
        new SpringStartTest().run(ActoconfigureTest.class);
    }
}
