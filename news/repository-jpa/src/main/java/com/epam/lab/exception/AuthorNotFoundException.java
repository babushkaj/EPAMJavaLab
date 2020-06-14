package com.epam.lab.exception;

public class AuthorNotFoundException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public AuthorNotFoundException(String message) {
        super(message);
    }

}

