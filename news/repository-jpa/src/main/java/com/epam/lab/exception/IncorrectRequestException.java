package com.epam.lab.exception;

public class IncorrectRequestException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public IncorrectRequestException(String message) {
        super(message);
    }

}
