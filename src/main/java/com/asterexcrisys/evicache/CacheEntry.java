package com.asterexcrisys.evicache;

public interface CacheEntry<K, V> {

    K key();

    V value();

}