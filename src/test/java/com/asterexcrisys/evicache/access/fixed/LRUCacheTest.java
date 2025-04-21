package com.asterexcrisys.evicache.access.fixed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LRUCacheTest {

    private LRUCache<String, Integer> cache;

    @BeforeEach
    public void setUp() {
        cache = new LRUCache<>(5);
    }

    @Test
    void shouldStoreAndRetrieveMultipleEntries() {
        cache.put("alpha", 0);
        cache.put("beta", 0);
        cache.put("gamma", 0);
        cache.put("alpha", 1);
        cache.put("beta", 2);
        cache.put("gamma", 3);
        assertEquals(1, cache.get("alpha"));
        assertEquals(2, cache.get("beta"));
        assertEquals(3, cache.get("gamma"));
        assertEquals(3, cache.size());
    }

    @Test
    public void shouldRemoveAndNotRetrieveMultipleEntries() {
        cache.put("alpha", 1);
        cache.put("beta", 2);
        cache.put("gamma", 3);
        cache.remove("alpha");
        cache.remove("gamma");
        assertNull(cache.get("alpha"));
        assertEquals(2, cache.get("beta"));
        assertNull(cache.get("gamma"));
        assertEquals(1, cache.size());
    }

    @Test
    public void shouldPeekAndNotRemoveMultipleEntries() {
        cache.put("alpha", 1);
        cache.put("beta", 2);
        cache.put("gamma", 3);
        assertEquals(3, cache.peekTop());
        assertEquals(1, cache.peekBottom());
        assertEquals(1, cache.get("alpha"));
        assertEquals(3, cache.get("gamma"));
        assertEquals(3, cache.size());
    }

    @Test
    public void shouldPopAndRemoveMultipleEntries() {
        cache.put("alpha", 1);
        cache.put("beta", 2);
        cache.put("gamma", 3);
        assertEquals(3, cache.popTop());
        assertEquals(1, cache.popBottom());
        assertNull(cache.get("alpha"));
        assertNull(cache.get("gamma"));
        assertEquals(1, cache.size());
    }

    @Test
    public void shouldEvictLeastRecentlyUsedWhenCacheIsFull() {
        cache.put("alpha", 1);
        cache.put("beta", 2);
        cache.put("gamma", 3);
        cache.put("delta", 4);
        cache.put("epsilon", 5);
        cache.put("eta", 6);
        cache.put("zeta", 7);
        assertNull(cache.get("alpha"));
        assertNull(cache.get("beta"));
        assertEquals(7, cache.peekTop());
        assertEquals(3, cache.peekBottom());
        assertEquals(5, cache.size());
    }

}