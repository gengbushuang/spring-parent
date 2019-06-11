package test.com.springboot.autoconfig.nrpc.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nrpc.client")
public class NrpcClientProperties {

    private int port = 9090;

    private String address = "0.0.0.0";

    public NrpcClientProperties(){}

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
