package com.annotation.model;

public class Life {

    public Life(){
        System.out.println("Life bean Constructor....");
    }

    public void init(){
        System.out.println("Life bean init....");
    }

    public void destroy(){
        System.out.println("Life bean destroy....");
    }
}
