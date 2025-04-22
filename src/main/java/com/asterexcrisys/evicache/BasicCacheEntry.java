package com.asterexcrisys.evicache;

public record BasicCacheEntry<K, V>(K key, V value) implements CacheEntry<K, V> {

    // All necessary methods are implemented by default

}