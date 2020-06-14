package com.epam.lab.exception;

public class UserNotFoundException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
