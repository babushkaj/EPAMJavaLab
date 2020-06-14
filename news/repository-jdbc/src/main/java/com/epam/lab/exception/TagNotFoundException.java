package com.epam.lab.exception;

public class TagNotFoundException extends RuntimeException {
    static final long serialVersionUID = 1L;

    private String message;

    public TagNotFoundException() {
        super();
    }

    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
