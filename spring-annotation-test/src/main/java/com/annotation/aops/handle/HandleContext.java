package com.annotation.aops.handle;

import java.util.List;

public class HandleContext {

    private List<AopHandle> handleList;

    private int currentIndex = -1;

    public HandleContext(List<AopHandle> handleList) {
        this.handleList = handleList;
    }

    public void proceed() {
        if (currentIndex == handleList.size() - 1) {
            this.handle();
            return;
        }
        AopHandle aopHandle = handleList.get(++currentIndex);

        aopHandle.handle(this);
    }

    private void handle() {
        throw new RuntimeException("报错");
//        System.out.println("HandleContext handle;");
    }
}
