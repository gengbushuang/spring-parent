package com.annotation.aops.handle;

public class ThrowHandle extends AopHandle {
    @Override
    public void handle(HandleContext context) {
        try {
            context.proceed();
        }catch (Exception e){
            System.out.println("ThrowHandle handle");
        }
    }
}
