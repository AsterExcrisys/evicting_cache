package com.asterexcrisys.evicache.fixed;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class LRUCache<K, V> {

    private final K[] keys;
    private final V[] values;
    private int size;

    @SuppressWarnings("unchecked")
    public LRUCache(int capacity) throws IllegalArgumentException {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity cannot be zero or negative");
        }
        keys = (K[]) new Object[capacity];
        values = (V[]) new Object[capacity];
        size = 0;
        clear();
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return keys.length;
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
        return search(key) != -1;
    }

    public V peekTop() {
        if (keys[0] == null) {
            return null;
        }
        return values[0];
    }

    public V peekBottom() {
        if (keys[size - 1] == null) {
            return null;
        }
        return values[size - 1];
    }

    public V elementTop() throws NoSuchElementException {
        if (keys[0] == null) {
            throw new NoSuchElementException("cannot peek an empty cache");
        }
        return values[0];
    }

    public V elementBottom() throws NoSuchElementException {
        if (keys[size - 1] == null) {
            throw new NoSuchElementException("cannot peek an empty cache");
        }
        return values[size - 1];
    }

    public V popTop() {
        if (keys[0] == null) {
            return null;
        }
        V top = values[0];
        remove(keys[0]);
        return top;
    }

    public V popBottom() {
        if (keys[size - 1] == null) {
            return null;
        }
        V bottom = values[size - 1];
        remove(keys[size - 1]);
        return bottom;
    }

    public V pollTop() throws NoSuchElementException {
        if (keys[0] == null) {
            throw new NoSuchElementException("cannot pop an empty cache");
        }
        V top = values[0];
        remove(keys[0]);
        return top;
    }

    public V pollBottom() throws NoSuchElementException {
        if (keys[size - 1] == null) {
            throw new NoSuchElementException("cannot pop an empty cache");
        }
        V bottom = values[size - 1];
        remove(keys[size - 1]);
        return bottom;
    }

    public V get(K key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        int index = search(key);
        if (index >= 0) {
            V value = values[index];
            for (int i = index - 1; i >= 0; i--) {
                keys[i + 1] = keys[i];
                values[i + 1] = values[i];
            }
            keys[0] = key;
            values[0] = value;
            return value;
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
        int index = search(key);
        if (index >= 0) {
            for (int i = index - 1; i >= 0; i--) {
                keys[i + 1] = keys[i];
                values[i + 1] = values[i];
            }
        } else {
            size++;
            for (int i = keys.length - 2; i >= 0; i--) {
                keys[i + 1] = keys[i];
                values[i + 1] = values[i];
            }
        }
        keys[0] = key;
        values[0] = value;
    }

    public void remove(K key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        int index = search(key);
        if (index >= 0) {
            size--;
            for (int i = index + 1; i < keys.length; i++) {
                keys[i - 1] = keys[i];
                values[i - 1] = values[i];
            }
            keys[keys.length - 1] = null;
            values[keys.length - 1] = null;
        }
    }

    public void clear() {
        Arrays.fill(keys, null);
        Arrays.fill(values, null);
        size = 0;
    }

    private int search(K key) {
        for (int i = 0; i < size; i++) {
            if (keys[i] != null && keys[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LRUCache<?, ?> other)) {
            return false;
        }
        if (size != other.size) {
            return false;
        }
        if (keys.length != other.keys.length) {
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