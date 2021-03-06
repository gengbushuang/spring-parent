package test.com.springboot.autoconfig.grpc.server;

import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.discovery.EurekaClientConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.cloud.commons.util.IdUtils.getDefaultInstanceId;

@Configuration
@EnableConfigurationProperties
//先加载EurekaClientAutoConfiguration
@AutoConfigureBefore({EurekaClientAutoConfiguration.class})
public class EurekaAutoConfiguration {

    private static final String GRPC_PROVIDER_TYPE = "PROVIDER";
    private ConfigurableEnvironment env;
    private RelaxedPropertyResolver propertyResolver;
    private GrpcServerProperties grpcProperties;

    public EurekaAutoConfiguration(ConfigurableEnvironment env, GrpcServerProperties grpcProperties) {
        this.env = env;
        this.propertyResolver = new RelaxedPropertyResolver(env);
        this.grpcProperties = grpcProperties;
    }


    @Bean
    //查找EurekaClientConfig对象，仅搜索当前上下文，不存在EurekaClientConfig进行加载
    @ConditionalOnMissingBean(value = {EurekaClientConfig.class},search = SearchStrategy.CURRENT)
    public EurekaClientConfigBean eurekaClientConfigBean() {
        EurekaClientConfigBean client = new EurekaClientConfigBean();
        if ("bootstrap".equals(this.propertyResolver.getProperty("spring.config.name"))) {
            client.setRegisterWithEureka(false);
        }

        String serverUrl = this.env.getProperty("GRPC_EUREKA_CLIENT_SERVICE_URL");
        if (!StringUtils.isEmpty(serverUrl)) {
            Map<String, String> map = new HashMap();
            map.put("defaultZone", serverUrl);
            client.setServiceUrl(map);
        }

        return client;
    }

    @Bean
    @ConditionalOnMissingBean(value = {EurekaInstanceConfig.class},search = SearchStrategy.CURRENT)
    public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils inetUtils) throws MalformedURLException {
        PropertyResolver eurekaPropertyResolver = new RelaxedPropertyResolver(this.env, "eureka.instance.");
        String hostname = eurekaPropertyResolver.getProperty("hostname");
        boolean preferIpAddress = Boolean.parseBoolean(eurekaPropertyResolver.getProperty("preferIpAddress"));
        int nonSecurePort = Integer.valueOf(this.propertyResolver.getProperty("server.port", this.propertyResolver.getProperty("port", "8080")));
        int managementPort = Integer.valueOf(this.propertyResolver.getProperty("management.port", String.valueOf(nonSecurePort)));
        String managementContextPath = this.propertyResolver.getProperty("management.contextPath", this.propertyResolver.getProperty("server.contextPath", "/"));
        EurekaInstanceConfigBean instance = new EurekaInstanceConfigBean(inetUtils);
        instance.setNonSecurePort(nonSecurePort);
        instance.setInstanceId(getDefaultInstanceId(propertyResolver));
        instance.setPreferIpAddress(preferIpAddress);
        if (managementPort != nonSecurePort && managementPort != 0) {
            if (StringUtils.hasText(hostname)) {
                instance.setHostname(hostname);
            }
            String statusPageUrlPath = eurekaPropertyResolver.getProperty("statusPageUrlPath");
            String healthCheckUrlPath = eurekaPropertyResolver.getProperty("healthCheckUrlPath");
            if (!managementContextPath.endsWith("/")) {
                managementContextPath = managementContextPath + "/";
            }
            if (StringUtils.hasText(statusPageUrlPath)) {
                instance.setStatusPageUrlPath(statusPageUrlPath);
            }
            if (StringUtils.hasText(healthCheckUrlPath)) {
                instance.setHealthCheckUrlPath(healthCheckUrlPath);
            }
            String scheme = instance.getSecurePortEnabled() ? "https" : "http";
            URL base = new URL(scheme, instance.getHostname(), managementPort, managementContextPath);
            instance.setStatusPageUrl(new URL(base, StringUtils.trimLeadingCharacter(instance.getStatusPageUrlPath(), '/')).toString());
            instance.setHealthCheckUrl(new URL(base, StringUtils.trimLeadingCharacter(instance.getHealthCheckUrlPath(), '/')).toString());
        }
        instance.setPreferIpAddress(true);
        instance.setLeaseRenewalIntervalInSeconds(10);
        instance.setLeaseExpirationDurationInSeconds(30);

        instance.setInstanceId("PROVIDER:" + instance.getIpAddress() + ":" + this.grpcProperties.getPort());
        instance.getMetadataMap().put("gRPC.port", String.valueOf(this.grpcProperties.getPort()));
        instance.getMetadataMap().put("gRPC.type", "PROVIDER");
        return instance;
    }
}
