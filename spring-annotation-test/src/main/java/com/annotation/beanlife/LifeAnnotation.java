package com.annotation.beanlife;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class LifeAnnotation {

    public LifeAnnotation(){
        System.out.println("LifeAnnotation bean Constructor....");
    }

    @PostConstruct
    public void init(){
        System.out.println("LifeAnnotation bean init....");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("LifeAnnotation bean destroy....");
    }
}
