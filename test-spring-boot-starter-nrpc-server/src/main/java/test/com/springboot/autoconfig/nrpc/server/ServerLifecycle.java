package test.com.springboot.autoconfig.nrpc.server;

import org.springframework.context.SmartLifecycle;

public class ServerLifecycle implements SmartLifecycle {

    private final Object lock = new Object();

    private volatile NrpcService nrpcService;

    NrpcServiceFactory factory;

    public ServerLifecycle(NrpcServiceFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        this.stop();
        callback.run();
    }

    @Override
    public void start() {
        NrpcService localServer = this.nrpcService;
        if (localServer == null) {
            try {
                this.nrpcService = factory.create();
                this.nrpcService.start();
            } catch (Exception e) {

            }
        }

    }

    @Override
    public void stop() {
        NrpcService localServer = this.nrpcService;
        if (localServer != null) {
            localServer.shutdown();
            this.nrpcService = null;
        }
    }

    @Override
    public boolean isRunning() {
        return this.nrpcService == null ? false : !this.nrpcService.isShutdown();
    }

    @Override
    public int getPhase() {
        return 100000;
    }
}
