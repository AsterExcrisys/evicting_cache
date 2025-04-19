package com.asterexcrisys.evicache.fixed;

import java.util.Arrays;
import java.util.NoSuchElementException;

@SuppressWarnings("unused")
public class MRUCache<K, V> {

    private int size;
    private final int capacity;
    private final K[] keys;
    private final V[] values;

    @SuppressWarnings("unchecked")
    public MRUCache(int capacity) throws IllegalArgumentException {
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
        return Arrays.copyOfRange(keys, capacity - size, capacity);
    }

    public V[] values() {
        return Arrays.copyOfRange(values, capacity - size, capacity);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean has(K key) {
        return indexOf(key) != -1;
    }

    public V peekTop() {
        if (keys[capacity - size] == null) {
            return null;
        }
        return get(capacity - size);
    }

    public V peekBottom() {
        if (keys[capacity - 1] == null) {
            return null;
        }
        return get(capacity - 1);
    }

    public V elementTop() throws NoSuchElementException {
        if (keys[capacity - size] == null) {
            throw new NoSuchElementException("cannot peek an empty cache");
        }
        return get(capacity - size);
    }

    public V elementBottom() throws NoSuchElementException {
        if (keys[capacity - 1] == null) {
            throw new NoSuchElementException("cannot peek an empty cache");
        }
        return get(capacity - 1);
    }

    public V popTop() {
        if (keys[capacity - size] == null) {
            return null;
        }
        V top = values[capacity - size];
        remove(keys[capacity - size]);
        return top;
    }

    public V popBottom() {
        if (keys[capacity - 1] == null) {
            return null;
        }
        V bottom = values[capacity - 1];
        remove(keys[capacity - 1]);
        return bottom;
    }

    public V pollTop() throws NoSuchElementException {
        if (keys[capacity - size] == null) {
            throw new NoSuchElementException("cannot pop an empty cache");
        }
        V top = values[capacity - size];
        remove(keys[capacity - size]);
        return top;
    }

    public V pollBottom() throws NoSuchElementException {
        if (keys[capacity - 1] == null) {
            throw new NoSuchElementException("cannot pop an empty cache");
        }
        V bottom = values[capacity - 1];
        remove(keys[capacity - 1]);
        return bottom;
    }

    public V get(K key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        int index = indexOf(key);
        if (index >= 0) {
            V value = values[index];
            for (int i = index + 1; i < capacity; i++) {
                keys[i - 1] = keys[i];
                values[i - 1] = values[i];
            }
            keys[capacity - 1] = key;
            values[capacity - 1] = value;
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
        int index = indexOf(key);
        if (index >= 0) {
            for (int i = index + 1; i < capacity; i++) {
                keys[i - 1] = keys[i];
                values[i - 1] = values[i];
            }
        } else {
            if (size < capacity) {
                size++;
            }
            for (int i = 1; i < capacity; i++) {
                keys[i - 1] = keys[i];
                values[i - 1] = values[i];
            }
        }
        keys[capacity - 1] = key;
        values[capacity - 1] = value;
    }

    public void remove(K key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        int index = indexOf(key);
        if (index >= 0) {
            if (size > 0) {
                size--;
            }
            for (int i = index - 1; i >= 0; i--) {
                keys[i + 1] = keys[i];
                values[i + 1] = values[i];
            }
            keys[0] = null;
            values[0] = null;
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
        for (int i = capacity - size; i < capacity; i++) {
            if (keys[i] != null && keys[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private V get(int index) throws IndexOutOfBoundsException {
        if (index < capacity - size || index > capacity - 1) {
            throw new IndexOutOfBoundsException();
        }
        K key = keys[index];
        V value = values[index];
        for (int i = index + 1; i < capacity; i++) {
            keys[i - 1] = keys[i];
            values[i - 1] = values[i];
        }
        keys[capacity - 1] = key;
        values[capacity - 1] = value;
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MRUCache<?, ?> other)) {
            return false;
        }
        if (size != other.size) {
            return false;
        }
        if (capacity != other.capacity) {
            return false;
        }
        for (int i = capacity - size; i < capacity; i++) {
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
        for (int i = capacity - size; i < capacity; i++) {
            builder.append(String.format("%s: %s", keys[i], values[i]));
            if (i < capacity - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return String.format("%s%s", super.toString(), builder);
    }

}