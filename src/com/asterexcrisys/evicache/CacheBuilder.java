package com.asterexcrisys.evicache;

import com.asterexcrisys.evicache.fixed.LFUCache;
import com.asterexcrisys.evicache.fixed.LRUCache;
import com.asterexcrisys.evicache.fixed.MFUCache;
import com.asterexcrisys.evicache.fixed.MRUCache;

public class CacheBuilder<K, V> {

    private EvictionPolicy policy;
    private boolean isFixed;
    private int capacity;

    private CacheBuilder() {
        policy = EvictionPolicy.LRU;
        isFixed = true;
        capacity = 100;
    }

    public CacheBuilder<K, V> evictionPolicy(EvictionPolicy policy) {
        this.policy = policy;
        return this;
    }

    public CacheBuilder<K, V> fixed(boolean isFixed) {
        this.isFixed = isFixed;
        return this;
    }

    public CacheBuilder<K, V> capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public Cache<K, V> build() throws IllegalArgumentException {
        return switch (policy) {
            case LRU -> {
                if (isFixed) {
                    yield new LRUCache<>(capacity);
                }
                yield null;
            }
            case LFU -> {
                if (isFixed) {
                    yield new LFUCache<>(capacity);
                }
                yield null;
            }
            case MRU -> {
                if (isFixed) {
                    yield new MRUCache<>(capacity);
                }
                yield null;
            }
            case MFU -> {
                if (isFixed) {
                    yield new MFUCache<>(capacity);
                }
                yield null;
            }
            default -> {
                throw new IllegalArgumentException("Invalid policy: " + policy);
            }
        };
    }

    public static <K, V> CacheBuilder<K, V> newBuilder() {
        return new CacheBuilder<>();
    }

}