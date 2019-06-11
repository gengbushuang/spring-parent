package test.com.springboot.autoconfig.nrpc.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface NrpcServer {
    Class<?> value();
}
