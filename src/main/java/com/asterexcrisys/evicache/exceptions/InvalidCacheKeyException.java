package com.asterexcrisys.evicache.exceptions;

public class InvalidCacheKeyException extends RuntimeException {

    public InvalidCacheKeyException() {
        super();
    }

    public InvalidCacheKeyException(String message) {
        super(message);
    }

}