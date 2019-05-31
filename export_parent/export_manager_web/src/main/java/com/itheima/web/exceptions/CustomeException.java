package com.itheima.web.exceptions;

/**
 * 自定义异常
 */
public class CustomeException extends Exception {

    private String message;

    public CustomeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
