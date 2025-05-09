package com.asterexcrisys.evicache.maps.extra.fixed;

import com.asterexcrisys.evicache.Cache;
import com.asterexcrisys.evicache.CacheEntry;
import com.asterexcrisys.evicache.CacheRecorder;
import com.asterexcrisys.evicache.entries.PriorityCacheEntry;
import com.asterexcrisys.evicache.exceptions.CacheUnderflowException;
import com.asterexcrisys.evicache.exceptions.IllegalCacheStateException;
import com.asterexcrisys.evicache.exceptions.InvalidCacheEntryException;
import java.util.Arrays;
import java.util.HashMap;

@SuppressWarnings({"unused", "Duplicates"})
public class PriorityCache<K, V> implements Cache<K, V> {

    private int size;
    private final int capacity;
    private final boolean metricsEnabled;
    private final K[] keys;
    private final V[] values;
    private final Integer[] priorities;
    private final CacheRecorder recorder;

    @SuppressWarnings("unchecked")
    public PriorityCache(int capacity, boolean metricsEnabled) throws IllegalCacheStateException {
        if (capacity < 1) {
            throw new IllegalCacheStateException("capacity cannot be zero or negative");
        }
        size = 0;
        this.capacity = capacity;
        this.metricsEnabled = metricsEnabled;
        keys = (K[]) new Object[this.capacity];
        values = (V[]) new Object[this.capacity];
        priorities = new Integer[this.capacity];
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

    public Integer[] priorities() {
        return Arrays.copyOf(priorities, size);
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

    public void put(K key, V value, Integer priority) throws InvalidCacheEntryException {
        if (key == null) {
            throw new InvalidCacheEntryException("key cannot be null");
        }
        if (priority == null) {
            throw new InvalidCacheEntryException("priority cannot be null");
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
            priorities[index] = priority;
            sort(index);
        } else {
            if (size < capacity) {
                size++;
            } else {
                if (priorities[size - 1] >= priority) {
                    return;
                }
                if (metricsEnabled) {
                    recorder.eviction();
                }
            }
            keys[size - 1] = key;
            values[size - 1] = value;
            priorities[size - 1] = priority;
            sort(size - 1);
        }
    }

    public void put(CacheEntry<K, V> entry) throws IllegalCacheStateException, InvalidCacheEntryException {
        if (!(entry instanceof PriorityCacheEntry<K, V> priorityEntry)) {
            throw new IllegalCacheStateException("entry must be of 'PriorityCacheEntry' type");
        }
        put(priorityEntry.key(), priorityEntry.value(), priorityEntry.priority());
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
        Arrays.fill(priorities, null);
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
        priorities[index]++;
        sort(index);
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
            priorities[i - 1] = priorities[i];
        }
        keys[size] = null;
        values[size] = null;
        priorities[size] = null;
    }

    private void sort(int start) throws IndexOutOfBoundsException {
        if (start < 0 || start > size - 1) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }
        if (start > 0 && priorities[start] > priorities[start - 1]) {
            for (int i = start; i >= 1; i--) {
                if (priorities[i - 1] > priorities[i]) {
                    break;
                }
                swap(keys, i - 1, i);
                swap(values, i - 1, i);
                swap(priorities, i - 1, i);
            }
        }
        if (start < size - 1 && priorities[start] < priorities[start + 1]) {
            for (int i = start; i < size - 1; i++) {
                if (priorities[i + 1] < priorities[i]) {
                    break;
                }
                swap(keys, i + 1, i);
                swap(values, i + 1, i);
                swap(priorities, i + 1, i);
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
        if (!(object instanceof PriorityCache<?, ?> other)) {
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
            if (!priorities[i].equals(other.priorities[i])) {
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
            builder.append(String.format("%s: %s (%s)", keys[i], values[i], priorities[i]));
            if (i < size - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return String.format("%s%s", super.toString(), builder);
    }

}