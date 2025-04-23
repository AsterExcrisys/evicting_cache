package com.asterexcrisys.evicache.maps.access.fixed;

import com.asterexcrisys.evicache.entries.BasicCacheEntry;
import com.asterexcrisys.evicache.Cache;
import com.asterexcrisys.evicache.CacheBuilder;
import com.asterexcrisys.evicache.models.EvictionPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LRUCacheTest {

    private Cache<String, Integer> cache;

    @BeforeEach
    public void setUp() {
        cache = CacheBuilder.<String, Integer>newBuilder().evictionPolicy(EvictionPolicy.LRU).capacityFixed(true).initialCapacity(5).build();
    }

    @Test
    void shouldStoreAndRetrieveMultipleEntries() {
        cache.put(new BasicCacheEntry<>("alpha", 0));
        cache.put(new BasicCacheEntry<>("beta", 0));
        cache.put(new BasicCacheEntry<>("gamma", 0));
        cache.put(new BasicCacheEntry<>("alpha", 1));
        cache.put(new BasicCacheEntry<>("beta", 2));
        cache.put(new BasicCacheEntry<>("gamma", 3));
        assertEquals(1, cache.get("alpha"));
        assertEquals(2, cache.get("beta"));
        assertEquals(3, cache.get("gamma"));
        assertEquals(3, cache.size());
    }

    @Test
    public void shouldRemoveAndNotRetrieveMultipleEntries() {
        cache.put(new BasicCacheEntry<>("alpha", 1));
        cache.put(new BasicCacheEntry<>("beta", 2));
        cache.put(new BasicCacheEntry<>("gamma", 3));
        cache.remove("alpha");
        cache.remove("gamma");
        assertNull(cache.get("alpha"));
        assertEquals(2, cache.get("beta"));
        assertNull(cache.get("gamma"));
        assertEquals(1, cache.size());
    }

    @Test
    public void shouldPeekAndNotRemoveMultipleEntries() {
        cache.put(new BasicCacheEntry<>("alpha", 1));
        cache.put(new BasicCacheEntry<>("beta", 2));
        cache.put(new BasicCacheEntry<>("gamma", 3));
        assertEquals(3, cache.peekTop());
        assertEquals(1, cache.peekBottom());
        assertEquals(1, cache.get("alpha"));
        assertEquals(3, cache.get("gamma"));
        assertEquals(3, cache.size());
    }

    @Test
    public void shouldPopAndRemoveMultipleEntries() {
        cache.put(new BasicCacheEntry<>("alpha", 1));
        cache.put(new BasicCacheEntry<>("beta", 2));
        cache.put(new BasicCacheEntry<>("gamma", 3));
        assertEquals(3, cache.popTop());
        assertEquals(1, cache.popBottom());
        assertNull(cache.get("alpha"));
        assertNull(cache.get("gamma"));
        assertEquals(1, cache.size());
    }

    @Test
    public void shouldEvictLeastRecentlyUsedWhenCacheIsFull() {
        cache.put(new BasicCacheEntry<>("alpha", 1));
        cache.put(new BasicCacheEntry<>("beta", 2));
        cache.put(new BasicCacheEntry<>("gamma", 3));
        cache.put(new BasicCacheEntry<>("delta", 4));
        cache.put(new BasicCacheEntry<>("epsilon", 5));
        cache.put(new BasicCacheEntry<>("eta", 6));
        cache.put(new BasicCacheEntry<>("zeta", 7));
        assertNull(cache.get("alpha"));
        assertNull(cache.get("beta"));
        assertEquals(7, cache.peekTop());
        assertEquals(3, cache.peekBottom());
        assertEquals(5, cache.size());
    }

}