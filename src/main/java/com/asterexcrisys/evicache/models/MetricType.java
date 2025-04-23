package com.asterexcrisys.evicache.models;

public enum MetricType {

    CACHE_TYPE("type"),
    CACHE_HITS("hits"),
    CACHE_MISSES("misses"),
    CACHE_PUTS("puts"),
    CACHE_REMOVES("removes"),
    CACHE_EVICTIONS("evictions"),
    CACHE_CLEARS("clears"),
    CACHE_SIZE("size"),
    CACHE_CAPACITY("capacity");

    private final String identifier;

    MetricType(String identifier) {
        this.identifier = identifier;
    }

    public String identifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return identifier;
    }

}