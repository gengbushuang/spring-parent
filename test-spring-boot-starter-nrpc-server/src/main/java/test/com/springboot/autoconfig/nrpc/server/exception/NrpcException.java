package test.com.springboot.autoconfig.nrpc.server.exception;

public class NrpcException extends RuntimeException{
    public NrpcException(String message) {
        super(message);
    }

    public NrpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
