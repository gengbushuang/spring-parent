package com.annotation.aops;

public class TestAop {

    public int div(int i, int j) {
        System.out.println("TestAop...div...");
        return i / j;
    }
}
