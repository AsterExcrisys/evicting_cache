package com.asterexcrisys.evicache;

import com.asterexcrisys.evicache.access.fixed.*;
import com.asterexcrisys.evicache.extra.fixed.PriorityCache;
import com.asterexcrisys.evicache.extra.fixed.RandomCache;
import com.asterexcrisys.evicache.frequency.fixed.LFUCache;
import com.asterexcrisys.evicache.frequency.fixed.MFUCache;
import com.asterexcrisys.evicache.order.fixed.FIFOCache;
import com.asterexcrisys.evicache.order.fixed.LIFOCache;
import java.util.concurrent.TimeUnit;

public class CacheBuilder<K, V> {

    private EvictionPolicy policy;
    private long time;
    private TimeUnit unit;
    private boolean isFixed;
    private int capacity;

    private CacheBuilder() {
        policy = EvictionPolicy.LRU;
        time = 10L;
        unit = TimeUnit.MINUTES;
        isFixed = true;
        capacity = 100;
    }

    public CacheBuilder<K, V> evictionPolicy(EvictionPolicy policy) throws IllegalArgumentException {
        if (policy == null) {
            throw new IllegalArgumentException("policy cannot be null");
        }
        this.policy = policy;
        return this;
    }

    public CacheBuilder<K, V> expireTime(long time, TimeUnit unit) throws IllegalArgumentException {
        if (time < 0) {
            throw new IllegalArgumentException("time cannot be negative");
        }
        if (unit == null) {
            throw new IllegalArgumentException("unit cannot be null");
        }
        this.time = time;
        this.unit = unit;
        return this;
    }

    public CacheBuilder<K, V> fixedCapacity(boolean isFixed) {
        this.isFixed = isFixed;
        return this;
    }

    public CacheBuilder<K, V> initialCapacity(int capacity) throws IllegalArgumentException {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity cannot be zero or negative");
        }
        this.capacity = capacity;
        return this;
    }

    public Cache<K, V> build() {
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
            case FIFO -> {
                if (isFixed) {
                    yield new FIFOCache<>(capacity);
                }
                yield null;
            }
            case LIFO -> {
                if (isFixed) {
                    yield new LIFOCache<>(capacity);
                }
                yield null;
            }
            case TIME -> {
                if (isFixed) {
                    yield null;
                }
                yield null;
            }
            case EXPIRE -> {
                if (isFixed) {
                    yield null;
                }
                yield null;
            }
            case PRIORITY -> {
                if (isFixed) {
                    yield new PriorityCache<>(capacity);
                }
                yield null;
            }
            case RANDOM -> {
                if (isFixed) {
                    yield new RandomCache<>(capacity);
                }
                yield null;
            }
        };
    }

    public static <K, V> CacheBuilder<K, V> newBuilder() {
        return new CacheBuilder<>();
    }

}