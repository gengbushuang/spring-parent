package test.com.springboot.autoconfig.nrpc.server;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnnotationNrpcService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

    public Map<String, Object> findNrpcServers() {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(NrpcServer.class);
        Map<String, Object> handlerMap;
        if (beans != null && !beans.isEmpty()) {
            handlerMap = new HashMap<>(beans.size());
            for (Object serviceBean : beans.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(NrpcServer.class).value().getName();
                handlerMap.put(interfaceName, serviceBean);
            }
        } else {
            handlerMap = Collections.EMPTY_MAP;
        }
        return handlerMap;
    }
}
