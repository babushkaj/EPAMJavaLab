package com.epam.lab.exception;

public class NewsNotFoundException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public NewsNotFoundException(String message) {
        super(message);
    }

}
