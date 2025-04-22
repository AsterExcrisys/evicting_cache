package com.asterexcrisys.evicache.exceptions;

public class IllegalCacheStateException extends RuntimeException {

    public IllegalCacheStateException() {
        super();
    }

    public IllegalCacheStateException(String message) {
        super(message);
    }

}