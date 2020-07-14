package com.annotation.aops.handle;

public class AfterHandle extends AopHandle {
    @Override
    public void handle(HandleContext context) {
        try {
            context.proceed();
        }finally {
            System.out.println("AfterHandle handle");
        }
    }
}
