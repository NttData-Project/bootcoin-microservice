package com.demo.app.bootcoin.exception;

public class CustomNotFoundException extends RuntimeException {

    public CustomNotFoundException(String message) {
        super(message);
    }
}