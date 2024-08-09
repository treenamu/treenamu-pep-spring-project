package com.example.exception;

public class DuplicateUsernameException extends Exception {
    private String message;

    public DuplicateUsernameException() {}

    public DuplicateUsernameException(String message) {
        super(message);
        this.message = message;
    }
}
