package com.asterexcrisys.evicache.maps.frequency.fixed;

import com.asterexcrisys.evicache.entries.BasicCacheEntry;
import com.asterexcrisys.evicache.Cache;
import com.asterexcrisys.evicache.CacheBuilder;
import com.asterexcrisys.evicache.models.EvictionPolicy;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class LFUCacheBenchmark {

    private Cache<Integer, Integer> cache;

    @Setup(Level.Iteration)
    public void setup() {
        cache = CacheBuilder.<Integer, Integer>newBuilder().evictionPolicy(EvictionPolicy.LFU).capacityFixed(true).initialCapacity(1000).build();
        for (int i = 0; i < 1000; i++) {
            cache.put(new BasicCacheEntry<>(i, i));
        }
    }

    @Benchmark
    public Integer benchmarkGetOperation() {
        return cache.get(500);
    }

    @Benchmark
    public void benchmarkPutOperation() {
        cache.put(new BasicCacheEntry<>(1000, 1000));
    }

    @Benchmark
    public void benchmarkRemoveOperation() {
        cache.remove(300);
    }

}