package test.com.springboot.autoconfig.grpc.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "grpc.server")
public class GrpcServerProperties {

    public GrpcServerProperties() {
    }

    private int port = 9090;

    private String address = "0.0.0.0";

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
