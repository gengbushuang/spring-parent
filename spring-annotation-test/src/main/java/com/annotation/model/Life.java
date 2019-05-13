package com.annotation.model;

public class Life {

    public Life(){
        System.out.println("bean Constructor....");
    }

    public void init(){
        System.out.println("bean init....");
    }

    public void destroy(){
        System.out.println("bean destroy....");
    }
}
