package com.asterexcrisys.evicache.entries;

import com.asterexcrisys.evicache.CacheEntry;

public record PriorityCacheEntry<K, V>(K key, V value, Integer priority) implements CacheEntry<K, V> {

    // All necessary methods are implemented by default

}