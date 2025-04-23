package com.asterexcrisys.evicache.exceptions;

public class InvalidCacheEntryException extends RuntimeException {

    public InvalidCacheEntryException() {
        super();
    }

    public InvalidCacheEntryException(String message) {
        super(message);
    }

}