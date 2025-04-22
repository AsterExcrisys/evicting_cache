package com.asterexcrisys.evicache.models;

public enum EvictionPolicy {
    LRU,
    LFU,
    MRU,
    MFU,
    FIFO,
    LIFO,
    TIME,
    EXPIRE,
    PRIORITY,
    RANDOM
}