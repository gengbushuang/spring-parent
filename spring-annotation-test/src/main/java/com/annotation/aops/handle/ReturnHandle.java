package com.annotation.aops.handle;

public class ReturnHandle extends AopHandle {
    @Override
    public void handle(HandleContext context) {
        context.proceed();
        System.out.println("ReturnHandle handle");
    }
}
