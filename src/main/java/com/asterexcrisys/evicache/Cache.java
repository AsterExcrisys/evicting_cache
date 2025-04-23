package com.asterexcrisys.evicache;

import com.asterexcrisys.evicache.exceptions.CacheUnderflowException;
import com.asterexcrisys.evicache.exceptions.IllegalCacheStateException;
import com.asterexcrisys.evicache.exceptions.InvalidCacheEntryException;

/**
 * A generic cache interface that supports retrieval, insertion, and removal of elements
 * by key, with optional access to the top and bottom elements, suggesting an ordered
 * eviction strategy (e.g. LRU or MRU).
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
@SuppressWarnings("unused")
public interface Cache<K, V> {

    /**
     * Returns the current number of key-value pairs in the cache.
     *
     * @return the number of entries in the cache
     */
    int size();

    /**
     * Returns the maximum number of elements this cache can hold.
     *
     * @return the maximum capacity of the cache
     */
    int capacity();

    /**
     * Returns an array of the keys currently in the cache.
     *
     * @return an array containing all keys in the cache
     */
    K[] keys();

    /**
     * Returns an array of the values currently in the cache.
     *
     * @return an array containing all values in the cache
     */
    V[] values();

    /**
     * Checks if the cache is empty.
     *
     * @return {@code true} if the cache contains no entries, otherwise {@code false}
     */
    boolean isEmpty();

    /**
     * Checks if the cache contains a value for the specified key.
     *
     * @param key the key whose presence is to be tested
     * @return {@code true} if the cache contains a mapping for the key
     */
    boolean has(K key);

    /**
     * Retrieves the value at the top of the cache without removing it, or returns {@code null}
     * if the cache is empty.
     *
     * @return the top value or {@code null} if the cache is empty
     */
    V peekTop();

    /**
     * Retrieves the value at the bottom of the cache without removing it, or returns {@code null}
     * if the cache is empty.
     *
     * @return the bottom value or {@code null} if the cache is empty
     */
    V peekBottom();

    /**
     * Retrieves the value at the top of the cache without removing it.
     *
     * @return the top value
     * @throws CacheUnderflowException if the cache is empty
     */
    V elementTop() throws CacheUnderflowException;

    /**
     * Retrieves the value at the bottom of the cache without removing it.
     *
     * @return the bottom value
     * @throws CacheUnderflowException if the cache is empty
     */
    V elementBottom() throws CacheUnderflowException;

    /**
     * Removes and returns the value at the top of the cache.
     *
     * @return the removed top value, or {@code null} if the cache is empty
     */
    V popTop();

    /**
     * Removes and returns the value at the bottom of the cache.
     *
     * @return the removed bottom value, or {@code null} if the cache is empty
     */
    V popBottom();

    /**
     * Removes and returns the value at the top of the cache.
     *
     * @return the removed top value
     * @throws CacheUnderflowException if the cache is empty
     */
    V pollTop() throws CacheUnderflowException;

    /**
     * Removes and returns the value at the bottom of the cache.
     *
     * @return the removed bottom value
     * @throws CacheUnderflowException if the cache is empty
     */
    V pollBottom() throws CacheUnderflowException;

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value mapped to the key, or {@code null} if they key is not present
     * @throws InvalidCacheEntryException if the key is {@code null}
     */
    V get(K key) throws InvalidCacheEntryException;

    /**
     * Retrieves the value associated with the specified key, or returns the default value if the key is not found.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue the value to return if the key is not found
     * @return the value mapped to the key, or {@code defaultValue} if the key is not present
     * @throws InvalidCacheEntryException if the key is {@code null}
     */
    V get(K key, V defaultValue) throws InvalidCacheEntryException;

    /**
     * Associates the specified value with the specified key in the cache.
     * If the cache exceeds its capacity, an eviction strategy may be applied.
     *
     * @param entry the key-value pair to be added/updated
     * @throws IllegalCacheStateException if the entry is not of the correct type for the cache
     * @throws InvalidCacheEntryException if the key is {@code null}
     */
    void put(CacheEntry<K, V> entry) throws IllegalCacheStateException, InvalidCacheEntryException;

    /**
     * Removes the mapping for the specified key from the cache if present.
     *
     * @param key the key whose mapping is to be removed
     * @throws InvalidCacheEntryException if the key is {@code null}
     */
    void remove(K key) throws InvalidCacheEntryException;

    /**
     * Removes all entries from the cache.
     */
    void clear();

    /**
     * Compares the specified object with this cache for equality.
     *
     * @param object the object to be compared for equality with this cache
     * @return {@code true} if the specified object is equal to this cache
     */
    @Override
    boolean equals(Object object);

    /**
     * Returns a string representation of the cache, typically including its contents.
     *
     * @return a string representation of the cache
     */
    @Override
    String toString();

}