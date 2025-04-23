package com.asterexcrisys.evicache.maps.order.fixed;

import com.asterexcrisys.evicache.CacheRecorder;
import com.asterexcrisys.evicache.entries.BasicCacheEntry;
import com.asterexcrisys.evicache.Cache;
import com.asterexcrisys.evicache.CacheEntry;
import com.asterexcrisys.evicache.exceptions.CacheUnderflowException;
import com.asterexcrisys.evicache.exceptions.IllegalCacheStateException;
import com.asterexcrisys.evicache.exceptions.InvalidCacheEntryException;
import java.util.Arrays;
import java.util.HashMap;

@SuppressWarnings({"unused", "Duplicates"})
public class FIFOCache<K, V> implements Cache<K, V> {

    private int size;
    private final int capacity;
    private final boolean metricsEnabled;
    private final K[] keys;
    private final V[] values;
    private final CacheRecorder recorder;

    @SuppressWarnings("unchecked")
    public FIFOCache(int capacity, boolean metricsEnabled) throws IllegalCacheStateException {
        if (capacity < 1) {
            throw new IllegalCacheStateException("capacity cannot be zero or negative");
        }
        size = 0;
        this.capacity = capacity;
        this.metricsEnabled = metricsEnabled;
        keys = (K[]) new Object[this.capacity];
        values = (V[]) new Object[this.capacity];
        recorder = this.metricsEnabled? new CacheRecorder((Class<? extends Cache<?, ?>>) this.getClass()):null;
        clear();
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public boolean metricsEnabled() {
        return metricsEnabled;
    }

    public K[] keys() {
        return Arrays.copyOf(keys, size);
    }

    public V[] values() {
        return Arrays.copyOf(values, size);
    }

    public HashMap<String, Integer> metrics() throws IllegalCacheStateException {
        if (!metricsEnabled) {
            throw new IllegalCacheStateException("metrics are not enabled and therefore were not registered");
        }
        recorder.size(size);
        recorder.capacity(capacity);
        return recorder.metrics();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean has(K key) {
        return indexOf(key) != -1;
    }

    public V peekTop() {
        if (keys[0] == null) {
            return null;
        }
        return get(0);
    }

    public V peekBottom() {
        if (keys[size - 1] == null) {
            return null;
        }
        return get(size - 1);
    }

    public V elementTop() throws CacheUnderflowException {
        if (keys[0] == null) {
            throw new CacheUnderflowException("cannot peek an empty cache");
        }
        return get(0);
    }

    public V elementBottom() throws CacheUnderflowException {
        if (keys[size - 1] == null) {
            throw new CacheUnderflowException("cannot peek an empty cache");
        }
        return get(size - 1);
    }

    public V popTop() {
        if (keys[0] == null) {
            return null;
        }
        V top = values[0];
        remove(0);
        return top;
    }

    public V popBottom() {
        if (keys[size - 1] == null) {
            return null;
        }
        V bottom = values[size - 1];
        remove(size - 1);
        return bottom;
    }

    public V pollTop() throws CacheUnderflowException {
        if (keys[0] == null) {
            throw new CacheUnderflowException("cannot pop an empty cache");
        }
        V top = values[0];
        remove(0);
        return top;
    }

    public V pollBottom() throws CacheUnderflowException {
        if (keys[size - 1] == null) {
            throw new CacheUnderflowException("cannot pop an empty cache");
        }
        V bottom = values[size - 1];
        remove(size - 1);
        return bottom;
    }

    public V get(K key) throws InvalidCacheEntryException {
        if (key == null) {
            throw new InvalidCacheEntryException("key cannot be null");
        }
        int index = indexOf(key);
        if (index >= 0) {
            if (metricsEnabled) {
                recorder.hit();
            }
            return get(index);
        }
        if (metricsEnabled) {
            recorder.miss();
        }
        return null;
    }

    public V get(K key, V defaultValue) throws InvalidCacheEntryException {
        V value = get(key);
        return value == null? defaultValue:value;
    }

    public void put(K key, V value) throws InvalidCacheEntryException {
        if (key == null) {
            throw new InvalidCacheEntryException("key cannot be null");
        }
        if (metricsEnabled) {
            recorder.put();
        }
        int index = indexOf(key);
        if (index >= 0) {
            if (metricsEnabled) {
                recorder.hit();
            }
            values[index] = value;
        } else {
            if (size < capacity) {
                size++;
            } else {
                if (metricsEnabled) {
                    recorder.eviction();
                }
            }
            for (int i = size - 2; i >= 0; i--) {
                keys[i + 1] = keys[i];
                values[i + 1] = values[i];
            }
            keys[0] = key;
            values[0] = value;
        }
    }

    public void put(CacheEntry<K, V> entry) throws IllegalCacheStateException, InvalidCacheEntryException {
        if (!(entry instanceof BasicCacheEntry<K, V> basicEntry)) {
            throw new IllegalCacheStateException("entry must be of 'BasicCacheEntry' type");
        }
        put(basicEntry.key(), basicEntry.value());
    }

    public void remove(K key) throws InvalidCacheEntryException {
        if (key == null) {
            throw new InvalidCacheEntryException("key cannot be null");
        }
        int index = indexOf(key);
        if (index >= 0) {
            if (metricsEnabled) {
                recorder.hit();
                recorder.remove();
            }
            remove(index);
            return;
        }
        if (metricsEnabled) {
            recorder.miss();
        }
    }

    public void clear() {
        if (metricsEnabled) {
            recorder.clear();
        }
        Arrays.fill(keys, null);
        Arrays.fill(values, null);
        size = 0;
    }

    private int indexOf(K key) throws InvalidCacheEntryException {
        if (key == null) {
            throw new InvalidCacheEntryException("key cannot be null");
        }
        for (int i = 0; i < size; i++) {
            if (keys[i] != null && keys[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private V get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }
        return values[index];
    }

    private void remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }
        if (size > 0) {
            size--;
        }
        for (int i = index + 1; i < size + 1; i++) {
            keys[i - 1] = keys[i];
            values[i - 1] = values[i];
        }
        keys[size] = null;
        values[size] = null;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FIFOCache<?, ?> other)) {
            return false;
        }
        if (size != other.size) {
            return false;
        }
        if (capacity != other.capacity) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!keys[i].equals(other.keys[i])) {
                return false;
            }
            if (!values[i].equals(other.values[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < size; i++) {
            builder.append(String.format("%s: %s", keys[i], values[i]));
            if (i < size - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return String.format("%s%s", super.toString(), builder);
    }

}