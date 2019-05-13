package test.com.springboot.autoconfig.grpc.client;

import java.util.ArrayList;
import java.util.List;

public class GrpcChannelProperties {

    public static final String DEFAULT_ADDRS = "127.0.0.1";
    public static final int DEFAULT_PORT = 9090;
    public static final GrpcChannelProperties DEFAULT = new GrpcChannelProperties();
    private List<String> addrs = new ArrayList<String>() {
        {
            this.add("127.0.0.1:9090");
        }
    };

    private boolean plaintext = true;
    private boolean enableKeepAlive = false;
    private boolean keepAliveWithoutCalls = false;
    private long keepAliveTime = 180L;
    private long keepAliveTimeout = 20L;

    public GrpcChannelProperties() {
    }

    public List<String> getAddrs() {
        return addrs;
    }

    public void setAddrs(List<String> addrs) {
        this.addrs = addrs;
    }

    public boolean isPlaintext() {
        return plaintext;
    }

    public void setPlaintext(boolean plaintext) {
        this.plaintext = plaintext;
    }

    public boolean isEnableKeepAlive() {
        return enableKeepAlive;
    }

    public void setEnableKeepAlive(boolean enableKeepAlive) {
        this.enableKeepAlive = enableKeepAlive;
    }

    public boolean isKeepAliveWithoutCalls() {
        return keepAliveWithoutCalls;
    }

    public void setKeepAliveWithoutCalls(boolean keepAliveWithoutCalls) {
        this.keepAliveWithoutCalls = keepAliveWithoutCalls;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public long getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    public void setKeepAliveTimeout(long keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }
}
