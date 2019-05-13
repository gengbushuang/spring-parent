package com.annotation.beanlife;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.security.auth.Destroyable;

public class LifeImpl implements InitializingBean,DisposableBean {

    public LifeImpl(){
        System.out.println("LifeImpl bean Constructor....");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("LifeImpl bean destroy....");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("LifeImpl bean afterPropertiesSet....");
    }
}
