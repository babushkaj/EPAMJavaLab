package com.epam.lab.exception;

public class TagNotFoundException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public TagNotFoundException(String message) {
        super(message);
    }

}
