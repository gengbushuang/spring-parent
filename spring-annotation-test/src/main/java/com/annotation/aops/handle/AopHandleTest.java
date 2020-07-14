package com.annotation.aops.handle;

import java.util.ArrayList;
import java.util.List;

public class AopHandleTest {

    public static void main(String[] args) {
        List<AopHandle> handleList = new ArrayList<>();
        handleList.add(new ThrowHandle());
        handleList.add(new ReturnHandle());
        handleList.add(new AfterHandle());
        handleList.add(new BeforeHandle());

        HandleContext handleContext = new HandleContext(handleList);
        handleContext.proceed();
    }
}
