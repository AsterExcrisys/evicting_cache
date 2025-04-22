package com.asterexcrisys.evicache.exceptions;

public class CacheUnderflowException extends RuntimeException {

    public CacheUnderflowException() {
        super();
    }

    public CacheUnderflowException(String message) {
        super(message);
    }

}