package test.com.springboot.autoconfig.nrpc.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nrpc.server")
public class NrpcServiceProperties {

    private int port = 9090;

    private String address = "0.0.0.0";

    public NrpcServiceProperties(){}

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
