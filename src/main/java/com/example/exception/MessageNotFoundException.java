package com.example.exception;

public class MessageNotFoundException extends Exception {
    private String message;

    public MessageNotFoundException() {}

    public MessageNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
