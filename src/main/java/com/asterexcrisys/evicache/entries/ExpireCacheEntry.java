package com.asterexcrisys.evicache.entries;

import com.asterexcrisys.evicache.CacheEntry;

import java.util.concurrent.TimeUnit;

public record ExpireCacheEntry<K, V>(K key, V value, long time, TimeUnit unit) implements CacheEntry<K, V> {

    // All necessary methods are implemented by default

}