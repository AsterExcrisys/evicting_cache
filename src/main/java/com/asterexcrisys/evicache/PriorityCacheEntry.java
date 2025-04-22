package com.asterexcrisys.evicache;

public record PriorityCacheEntry<K, V>(K key, V value, Integer priority) implements CacheEntry<K, V> {

    // All necessary methods are implemented by default

}