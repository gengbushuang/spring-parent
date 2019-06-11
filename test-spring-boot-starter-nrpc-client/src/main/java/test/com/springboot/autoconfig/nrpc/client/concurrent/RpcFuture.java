package test.com.springboot.autoconfig.nrpc.client.concurrent;

import test.com.springboot.autoconfig.nrpc.client.model.RpcRequest;
import test.com.springboot.autoconfig.nrpc.client.model.RpcResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class RpcFuture implements Future<RpcResponse> {

    private RpcRequest request;

    private RpcResponse response;

    private Sync sync;

    public RpcFuture(RpcRequest request) {
        this.request = request;
        this.sync = new Sync();
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
        this.sync.release(1);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return this.sync.isDone();
    }

    @Override
    public RpcResponse get() throws InterruptedException, ExecutionException {
        this.sync.acquire(-1);
        if (this.response != null) {
            return this.response;
        }
        return null;
    }

    @Override
    public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = this.sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (this.response != null) {
                return this.response;
            }
        }
        return null;
    }

    private class Sync extends AbstractQueuedSynchronizer {

        private final int done = 1;
        private final int pending = 0;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == done;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == pending) {
                if (compareAndSetState(pending, done)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

        public boolean isDone() {
            return getState() == done;
        }
    }
}
