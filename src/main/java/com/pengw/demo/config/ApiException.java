package com.pengw.demo.config;

public class ApiException extends RuntimeException {
    private static final long serialVersionUID = -5885155226898287919L;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
