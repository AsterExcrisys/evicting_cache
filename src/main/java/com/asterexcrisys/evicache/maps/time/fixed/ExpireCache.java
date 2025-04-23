package com.asterexcrisys.evicache.maps.time.fixed;

import com.asterexcrisys.evicache.Cache;
import com.asterexcrisys.evicache.CacheEntry;
import com.asterexcrisys.evicache.entries.ExpireCacheEntry;
import com.asterexcrisys.evicache.exceptions.CacheUnderflowException;
import com.asterexcrisys.evicache.exceptions.IllegalCacheStateException;
import com.asterexcrisys.evicache.exceptions.InvalidCacheEntryException;
import com.asterexcrisys.evicache.models.ExpireMode;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unused", "Duplicates"})
public class ExpireCache<K, V> implements Cache<K, V> {

    private int size;
    private final int capacity;
    private final ExpireMode mode;
    private final K[] keys;
    private final V[] values;
    private final Long[] timestamps;

    @SuppressWarnings("unchecked")
    public ExpireCache(int capacity, ExpireMode mode) throws IllegalCacheStateException {
        if (capacity < 1) {
            throw new IllegalCacheStateException("capacity cannot be zero or negative");
        }
        if (mode == null) {
            throw new IllegalCacheStateException("mode cannot be null");
        }
        size = 0;
        this.capacity = capacity;
        this.mode = mode;
        keys = (K[]) new Object[this.capacity];
        values = (V[]) new Object[this.capacity];
        timestamps = new Long[this.capacity];
        clear();
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public ExpireMode mode() {
        return mode;
    }

    public K[] keys() {
        return Arrays.copyOf(keys, size);
    }

    public V[] values() {
        return Arrays.copyOf(values, size);
    }

    public Long[] timestamps() {
        return Arrays.copyOf(timestamps, size);
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
            if (timestamps[index] > Instant.now().toEpochMilli()) {
                remove(index);
                return null;
            }
            return get(index);
        }
        return null;
    }

    public V get(K key, V defaultValue) throws InvalidCacheEntryException {
        V value = get(key);
        return value == null? defaultValue:value;
    }

    public void put(K key, V value, long time, TimeUnit unit) throws InvalidCacheEntryException {
        if (key == null) {
            throw new InvalidCacheEntryException("key cannot be null");
        }
        if (time < 0) {
            throw new InvalidCacheEntryException("time cannot be negative");
        }
        if (unit == null) {
            throw new InvalidCacheEntryException("unit cannot be null");
        }
        int index = indexOf(key);
        if (index >= 0) {
            values[index] = value;
            if (timestamps[index] > Instant.now().toEpochMilli() || mode == ExpireMode.AFTER_ACCESS || mode == ExpireMode.AFTER_UPDATE) {
                timestamps[index] = Instant.now().toEpochMilli() + unit.toMillis(time);
                sort(index);
            }
        } else {
            if (size < capacity) {
                size++;
            }
            keys[size - 1] = key;
            values[size - 1] = value;
            timestamps[size - 1] = Instant.now().toEpochMilli() + unit.toMillis(time);
            sort(size - 1);
        }
    }

    public void put(CacheEntry<K, V> entry) throws IllegalCacheStateException, InvalidCacheEntryException {
        if (!(entry instanceof ExpireCacheEntry<K, V> expireEntry)) {
            throw new IllegalCacheStateException("entry must be of 'ExpireCacheEntry' type");
        }
        put(expireEntry.key(), expireEntry.value(), expireEntry.time(), expireEntry.unit());
    }

    public void remove(K key) throws InvalidCacheEntryException {
        if (key == null) {
            throw new InvalidCacheEntryException("key cannot be null");
        }
        int index = indexOf(key);
        if (index >= 0) {
            remove(index);
        }
    }

    public void clear() {
        Arrays.fill(keys, null);
        Arrays.fill(values, null);
        Arrays.fill(timestamps, null);
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
        V value = values[index];
        if (mode == ExpireMode.AFTER_ACCESS) {
            timestamps[index] = Instant.now().toEpochMilli();
            sort(index);
        }
        return value;
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
            timestamps[i - 1] = timestamps[i];
        }
        keys[size] = null;
        values[size] = null;
        timestamps[size] = null;
    }

    private void sort(int start) throws IndexOutOfBoundsException {
        if (start < 0 || start > size - 1) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }
        if (start > 0 && timestamps[start] > timestamps[start - 1]) {
            for (int i = start; i >= 1; i--) {
                if (timestamps[i - 1] > timestamps[i]) {
                    break;
                }
                swap(keys, i - 1, i);
                swap(values, i - 1, i);
                swap(timestamps, i - 1, i);
            }
        }
        if (start < size - 1 && timestamps[start] < timestamps[start + 1]) {
            for (int i = start; i < size - 1; i++) {
                if (timestamps[i + 1] < timestamps[i]) {
                    break;
                }
                swap(keys, i + 1, i);
                swap(values, i + 1, i);
                swap(timestamps, i + 1, i);
            }
        }
    }

    private static <T> void swap(T[] array, int i, int j) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (array == null) {
            throw new IllegalArgumentException("array cannot be null");
        }
        if (i < 0 || j < 0 || i > array.length - 1 || j > array.length - 1) {
            throw new IndexOutOfBoundsException();
        }
        T tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ExpireCache<?, ?> other)) {
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
            if (!timestamps[i].equals(other.timestamps[i])) {
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
            builder.append(String.format("%s: %s (%s)", keys[i], values[i], timestamps[i]));
            if (i < size - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return String.format("%s%s", super.toString(), builder);
    }

}