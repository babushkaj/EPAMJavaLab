package com.epam.lab.exception;

import java.io.Serializable;

public class ExceptionEntity implements Serializable {
    static final long serialVersionUID = 1L;

    private String message;

    public ExceptionEntity(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
