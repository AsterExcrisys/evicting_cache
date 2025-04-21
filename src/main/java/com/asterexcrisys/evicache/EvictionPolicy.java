package com.asterexcrisys.evicache;

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