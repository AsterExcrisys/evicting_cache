package com.asterexcrisys.evicache;

import java.util.HashMap;

public class CacheRecorder {

    private int cacheHits;
    private int cacheMisses;
    private int cachePuts;
    private int cacheRemoves;
    private int cacheEvictions;
    private int cacheClears;
    private int cacheSize;
    private int cacheCapacity;
    
    public CacheRecorder() {
        cacheHits = 0;
        cacheMisses = 0;
        cachePuts = 0;
        cacheRemoves = 0;
        cacheEvictions = 0;
        cacheClears = 0;
        cacheSize = 0;
        cacheCapacity = 0;
    }
    
    public HashMap<String, Integer> metrics() {
        HashMap<String, Integer> metrics = new HashMap<>();
        metrics.put(MetricType.CACHE_HITS.identifier(), cacheHits);
        metrics.put(MetricType.CACHE_MISSES.identifier(), cacheMisses);
        metrics.put(MetricType.CACHE_PUTS.identifier(), cachePuts);
        metrics.put(MetricType.CACHE_REMOVES.identifier(), cacheRemoves);
        metrics.put(MetricType.CACHE_EVICTIONS.identifier(), cacheEvictions);
        metrics.put(MetricType.CACHE_CLEARS.identifier(), cacheClears);
        metrics.put(MetricType.CACHE_SIZE.identifier(), cacheSize);
        metrics.put(MetricType.CACHE_CAPACITY.identifier(), cacheCapacity);
        return metrics;
    }
    
    public void hit() {
        cacheHits++;
    }
    
    public void miss() {
        cacheMisses++;
    }
    
    public void put() {
        cachePuts++;
    }
    
    public void remove() {
        cacheRemoves++;
    }
    
    public void eviction() {
        cacheEvictions++;
    }
    
    public void clear() {
        cacheClears++;
    }
    
    public void size(int size) throws IllegalArgumentException {
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be negative");
        }
        cacheSize = size;
    }
    
    public void capacity(int capacity) throws IllegalArgumentException {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity cannot be zero or negative");
        }
        cacheCapacity = capacity;
    }

}