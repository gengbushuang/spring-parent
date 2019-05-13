package test.com.springboot.autoconfig.grpc.client.channel.discovery;

import com.google.common.base.Preconditions;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.Status;
import io.grpc.internal.LogExceptionRunnable;
import io.grpc.internal.SharedResourceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import javax.annotation.concurrent.GuardedBy;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DiscoveryClientNameResolver extends NameResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryClientNameResolver.class);

    private final String name;
    private final DiscoveryClient client;
    private final Attributes attributes;
    private final SharedResourceHolder.Resource<ScheduledExecutorService> timerServiceResource;
    private final SharedResourceHolder.Resource<ExecutorService> executorResource;
    @GuardedBy("this")
    private List<ServiceInstance> serviceInstanceList;
    @GuardedBy("this")
    private boolean shutdown;
    @GuardedBy("this")
    private boolean resolving;
    @GuardedBy("this")
    private ScheduledFuture<?> resolutionTask;
    @GuardedBy("this")
    private ScheduledExecutorService timerService;
    @GuardedBy("this")
    private ExecutorService executor;
    @GuardedBy("this")
    private Listener listener;

    private final Runnable resolutionRunnable = new Runnable() {
        @Override
        public void run() {
            LOGGER.info("resolution instance");
            Listener savedListener;
            synchronized (DiscoveryClientNameResolver.this) {
                if (resolutionTask != null) {
                    resolutionTask.cancel(false);
                    resolutionTask = null;
                }
                if (shutdown) {
                    return;
                }
                savedListener = listener;
                resolving = true;
            }
            //从eureka里面获取对应服务列表
            List<ServiceInstance> newServiceInstanceList = null;
            boolean isTask = false;
            try {
                try {
                    newServiceInstanceList = client.getInstances(name);
                } catch (Exception e) {
                    synchronized (DiscoveryClientNameResolver.this) {
//                        if (shutdown) {
//                            return;
//                        }
//                        resolutionTask = timerService.schedule(new LogExceptionRunnable(resolutionRunnableOnExecutor), 10, TimeUnit.SECONDS);
                        resolutionTime();
                        isTask=true;
                    }
                    savedListener.onError(Status.UNAVAILABLE.withCause(e));
                    return;
                }

                if (newServiceInstanceList == null || newServiceInstanceList.isEmpty()) {
                    savedListener.onError(Status.UNAVAILABLE.withCause(new RuntimeException("UNAVAILABLE: NameResolver returned an empty list")));
                } else {
                    if (!isNeedToUpdateServiceInstanceList(newServiceInstanceList)) {
                        synchronized (DiscoveryClientNameResolver.this) {
//                          resolutionTask = timerService.schedule(new LogExceptionRunnable(resolutionRunnableOnExecutor), 10, TimeUnit.SECONDS);
                            resolutionTime();
                            isTask=true;
                        }
                        return;
                    }
                    List<EquivalentAddressGroup> equivalentAddressGroups = new ArrayList();

                    serviceInstanceList.clear();
                    serviceInstanceList.addAll(newServiceInstanceList);

                    Iterator<ServiceInstance> newIterator = serviceInstanceList.iterator();
                    while (newIterator.hasNext()) {
                        ServiceInstance newInstance = newIterator.next();
                        Map<String, String> metadata = newInstance.getMetadata();
                        if (metadata.get("gRPC.port") != null) {
                            Integer port = Integer.valueOf((String) metadata.get("gRPC.port"));
                            LOGGER.info("find grpc service {} {}:{}", name, newInstance.getHost(), port);
                            EquivalentAddressGroup addressGroup = new EquivalentAddressGroup(new InetSocketAddress(newInstance.getHost(), port));
                            equivalentAddressGroups.add(addressGroup);
                        } else {
                            LOGGER.error("not find grpc service {} {}", name, newInstance.getServiceId());
                        }
                    }
                    savedListener.onAddresses(equivalentAddressGroups, Attributes.EMPTY);
                }
            } finally {
                synchronized (DiscoveryClientNameResolver.this) {
                    resolving = false;
                    if(!isTask){
//                        resolutionTask = timerService.schedule(new LogExceptionRunnable(resolutionRunnableOnExecutor), 10, TimeUnit.SECONDS);
                        resolutionTime();
                    }
                }
            }

        }
    };

    private final Runnable resolutionRunnableOnExecutor = new Runnable() {
        @Override
        public void run() {
            synchronized (DiscoveryClientNameResolver.this) {
                if (!shutdown) {
                    executor.execute(resolutionRunnable);
                }
            }
        }
    };


    public DiscoveryClientNameResolver(String name, DiscoveryClient client, Attributes attributes, SharedResourceHolder.Resource<ScheduledExecutorService> timerServiceResource, SharedResourceHolder.Resource<ExecutorService> executorResource) {
        this.name = name;
        this.client = client;
        this.attributes = attributes;
        this.timerServiceResource = timerServiceResource;
        this.executorResource = executorResource;
        this.serviceInstanceList = new ArrayList();
    }

    @Override
    public String getServiceAuthority() {
        return this.name;
    }

    @Override
    public final synchronized void start(Listener listener) {
        Preconditions.checkState(this.listener == null, "already started");
        this.timerService = SharedResourceHolder.get(this.timerServiceResource);
        this.executor = SharedResourceHolder.get(this.executorResource);
        this.listener = Preconditions.checkNotNull(listener, "listener");
        //定时刷新地址列表
//        this.timerService.scheduleWithFixedDelay(new LogExceptionRunnable(this.resolutionRunnableOnExecutor), 60L, 10L, TimeUnit.SECONDS);
        this.resolve();
    }


    @GuardedBy("this")
    private void resolve() {
        if (resolving || shutdown) {
            return;
        }
        executor.execute(resolutionRunnable);
    }

    @Override
    public final synchronized void refresh() {
        Preconditions.checkState(listener != null, "not started");
        resolve();
    }

    @Override
    public final synchronized void shutdown() {
        if (shutdown) {
            return;
        }
        shutdown = true;
        if (resolutionTask != null) {
            resolutionTask.cancel(false);
        }
        if (timerService != null) {
            timerService = SharedResourceHolder.release(timerServiceResource, timerService);
        }
        if (executor != null) {
            executor = SharedResourceHolder.release(executorResource, executor);
        }
    }

    private void resolutionTime(){
        if (shutdown) {
            return;
        }
        resolutionTask = timerService.schedule(new LogExceptionRunnable(resolutionRunnableOnExecutor), 10, TimeUnit.SECONDS);
    }

    private boolean isNeedToUpdateServiceInstanceList(List<ServiceInstance> newServiceInstanceList) {
        if (this.serviceInstanceList.size() != newServiceInstanceList.size()) {
            return true;
        }

        Iterator<ServiceInstance> oldIterator = this.serviceInstanceList.iterator();
        boolean isSame;
        do {
            if (!oldIterator.hasNext()) {
                return false;
            }
            ServiceInstance oldInstance = oldIterator.next();
            isSame = false;
            Iterator<ServiceInstance> newIterator = newServiceInstanceList.iterator();
            while (newIterator.hasNext()) {
                ServiceInstance newInstance = newIterator.next();
                if (newInstance.getHost().equals(oldInstance.getHost()) &&
                        newInstance.getPort() == oldInstance.getPort() &&
                        newInstance.getMetadata().get("gRPC.port").equals(oldInstance.getMetadata().get("gRPC.port"))) {
                    isSame = true;
                    break;
                }
            }
        } while (isSame);

        return true;
    }
}
