package com.epam.lab.exception;

public class AuthorNotFoundException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public AuthorNotFoundException() {
        super();
    }

    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}

