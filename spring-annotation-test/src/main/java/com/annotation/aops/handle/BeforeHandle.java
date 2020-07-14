package com.annotation.aops.handle;

public class BeforeHandle extends AopHandle {
    @Override
    public void handle(HandleContext context) {
        System.out.println("BeforeHandle handle");
        context.proceed();
    }
}
