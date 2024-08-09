package com.example.exception;

public class InvalidLoginException extends Exception {
    private String message;

    public InvalidLoginException() {}

    public InvalidLoginException(String message) {
        super(message);
        this.message = message;
    }
}
