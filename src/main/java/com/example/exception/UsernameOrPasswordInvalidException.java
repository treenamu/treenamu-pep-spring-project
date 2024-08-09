package com.example.exception;

public class UsernameOrPasswordInvalidException extends Exception {
    private String message;

    public UsernameOrPasswordInvalidException() {}

    public UsernameOrPasswordInvalidException(String message) {
        super(message);
        this.message = message;
    }
}
