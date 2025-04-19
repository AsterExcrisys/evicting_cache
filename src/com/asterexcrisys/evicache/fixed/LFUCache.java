package com.asterexcrisys.evicache.fixed;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
public class LFUCache<K, V> {

    private int size;
    private final int capacity;
    private final K[] keys;
    private final V[] values;
    private final Integer[] frequencies;

    @SuppressWarnings("unchecked")
    public LFUCache(int capacity) throws IllegalArgumentException {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity cannot be zero or negative");
        }
        size = 0;
        this.capacity = capacity;
        keys = (K[]) new Object[this.capacity];
        values = (V[]) new Object[this.capacity];
        frequencies = new Integer[this.capacity];
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

    public Integer[] frequencies() {
        return Arrays.copyOf(frequencies, size);
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
        int index = indexOf(key);
        if (index >= 0) {
            V value = values[index];
            frequencies[index]++;
            sort();
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
            values[index] = value;
            frequencies[index]++;
        } else {
            if (size < capacity) {
                size++;
            }
            keys[size - 1] = key;
            values[size - 1] = value;
            frequencies[size - 1] = 1;
        }
        sort();
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
            for (int i = index + 1; i < size + 1; i++) {
                keys[i - 1] = keys[i];
                values[i - 1] = values[i];
            }
            keys[size] = null;
            values[size] = null;
            frequencies[size] = null;
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

    private V get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        K key = keys[index];
        V value = values[index];
        for (int i = index - 1; i >= 0; i--) {
            keys[i + 1] = keys[i];
            values[i + 1] = values[i];
        }
        keys[0] = key;
        values[0] = value;
        return value;
    }

    private void sort() {
        if (size < 2) {
            return;
        }
        Integer[] indexes = new Integer[size];
        for (int i = 0; i < size; i++) {
            indexes[i] = i;
        }
        int maximum = frequencies[0];
        AtomicBoolean isSorted = new AtomicBoolean(true);
        Arrays.sort(indexes, Comparator.comparingInt((Integer index) -> {
            if (frequencies[index] > maximum) {
                isSorted.set(false);
            }
            return frequencies[index];
        }).reversed());
        if (isSorted.get()) {
            return;
        }
        K[] keys = keys();
        V[] values = values();
        Integer[] frequencies = frequencies();
        for (int i = 0; i < size; i++) {
            this.keys[i] = keys[indexes[i]];
            this.values[i] = values[indexes[i]];
            this.frequencies[i] = frequencies[indexes[i]];
        }
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LFUCache<?, ?> other)) {
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