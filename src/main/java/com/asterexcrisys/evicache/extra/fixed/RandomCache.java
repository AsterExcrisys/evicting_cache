package com.asterexcrisys.evicache.extra.fixed;

import com.asterexcrisys.evicache.BasicCacheEntry;
import com.asterexcrisys.evicache.Cache;
import com.asterexcrisys.evicache.CacheEntry;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

@SuppressWarnings({"unused", "Duplicates"})
public class RandomCache<K, V> implements Cache<K, V> {

    private int size;
    private final int capacity;
    private final K[] keys;
    private final V[] values;
    private static final Random random;

    static {
        random = new Random();
    }

    @SuppressWarnings("unchecked")
    public RandomCache(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity cannot be zero or negative");
        }
        size = 0;
        this.capacity = capacity;
        keys = (K[]) new Object[this.capacity];
        values = (V[]) new Object[this.capacity];
        clear();
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public K[] keys() {
        return Arrays.copyOf(keys, size);
    }

    public V[] values() {
        return Arrays.copyOf(values, size);
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

    public V elementTop() throws NoSuchElementException {
        if (keys[0] == null) {
            throw new NoSuchElementException("cannot peek an empty cache");
        }
        return get(0);
    }

    public V elementBottom() throws NoSuchElementException {
        if (keys[size - 1] == null) {
            throw new NoSuchElementException("cannot peek an empty cache");
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

    public V pollTop() throws NoSuchElementException {
        if (keys[0] == null) {
            throw new NoSuchElementException("cannot pop an empty cache");
        }
        V top = values[0];
        remove(0);
        return top;
    }

    public V pollBottom() throws NoSuchElementException {
        if (keys[size - 1] == null) {
            throw new NoSuchElementException("cannot pop an empty cache");
        }
        V bottom = values[size - 1];
        remove(size - 1);
        return bottom;
    }

    public V get(K key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        int index = indexOf(key);
        if (index >= 0) {
            return get(index);
        }
        return null;
    }

    public V get(K key, V defaultValue) throws IllegalArgumentException {
        V value = get(key);
        return value == null? defaultValue:value;
    }

    public void put(K key, V value) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        int index = indexOf(key);
        if (index >= 0) {
            values[index] = value;
        } else {
            if (size < capacity) {
                size++;
                keys[size - 1] = key;
                values[size - 1] = value;
                return;
            }
            index = nextIndex();
            keys[index] = key;
            values[index] = value;
        }
    }

    public void put(CacheEntry<K, V> entry) throws IllegalArgumentException {
        if (!(entry instanceof BasicCacheEntry<K, V> basicEntry)) {
            throw new IllegalArgumentException("entry must be of 'BasicCacheEntry' type");
        }
        put(basicEntry.key(), basicEntry.value());
    }

    public void remove(K key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        int index = indexOf(key);
        if (index >= 0) {
            remove(index);
        }
    }

    public void clear() {
        Arrays.fill(keys, null);
        Arrays.fill(values, null);
        size = 0;
    }

    private int indexOf(K key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        for (int i = 0; i < size; i++) {
            if (keys[i] != null && keys[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private int nextIndex() {
        return random.nextInt(capacity);
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
        if (!(object instanceof RandomCache<?, ?> other)) {
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