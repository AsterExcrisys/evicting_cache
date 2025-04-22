package com.asterexcrisys.evicache.entries;

import com.asterexcrisys.evicache.CacheEntry;

public record BasicCacheEntry<K, V>(K key, V value) implements CacheEntry<K, V> {

    // All necessary methods are implemented by default

}