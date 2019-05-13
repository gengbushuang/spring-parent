package test.com.springboot.autoconfig.grpc.server;

import io.grpc.Server;
import org.springframework.context.SmartLifecycle;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class GrpcServerLifecycle implements SmartLifecycle {

    private static AtomicInteger serverCounter = new AtomicInteger(-1);
    private volatile Server server;
    private volatile int phase = 2147483647;

    NettyGrpcServerFactory factory;


    public GrpcServerLifecycle(NettyGrpcServerFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean isAutoStartup() {
        System.out.println("grpc------------------->isAutoStartup");
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        System.out.println("grpc------------------->stop(Runnable callback)");
        this.stop();
        callback.run();
    }

    @Override
    public void start() {
        System.out.println("grpc------------------->start");
        try {
            this.createAndStartGrpcServer();
        } catch (IOException var2) {
            throw new IllegalStateException(var2);
        }
    }

    @Override
    public void stop() {
        System.out.println("grpc------------------->stop");
        this.stopAndReleaseGrpcServer();
    }

    @Override
    public boolean isRunning() {
        System.out.println("grpc------------------->isRunning");
        return this.server == null ? false : !this.server.isShutdown();
    }

    @Override
    public int getPhase() {
        System.out.println("grpc------------------->getPhase");
        return this.phase;
    }


    protected void createAndStartGrpcServer() throws IOException {
        Server localServer = this.server;
        if(localServer == null){
            this.server = this.factory.createServer();
            this.server.start();
            Thread awaitThread = new Thread("container-" + serverCounter.incrementAndGet()) {
                public void run() {
                    try {
                        GrpcServerLifecycle.this.server.awaitTermination();
                    } catch (InterruptedException var2) {
                        Thread.currentThread().interrupt();
                    }

                }
            };
            awaitThread.setDaemon(false);
            awaitThread.start();
        }
    }

    protected void stopAndReleaseGrpcServer() {
        Server localServer = this.server;
        if (localServer != null) {
            localServer.shutdown();
            this.server = null;
        }
    }
}
