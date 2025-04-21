package com.asterexcrisys.evicache;

import java.util.NoSuchElementException;

public interface Cache<K, V> {

    int size();

    int capacity();

    K[] keys();

    V[] values();

    boolean isEmpty();

    boolean has(K key);

    V peekTop();

    V peekBottom();

    V elementTop() throws NoSuchElementException;

    V elementBottom() throws NoSuchElementException;

    V popTop();

    V popBottom();

    V pollTop() throws NoSuchElementException;

    V pollBottom() throws NoSuchElementException;

    V get(K key) throws IllegalArgumentException;

    V get(K key, V defaultValue) throws IllegalArgumentException;

    void put(K key, V value) throws IllegalArgumentException;

    void remove(K key) throws IllegalArgumentException;

    void clear();

    @Override
    boolean equals(Object object);

    @Override
    String toString();

}