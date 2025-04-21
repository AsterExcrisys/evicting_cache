package com.asterexcrisys.evicache.access.fixed;

import com.asterexcrisys.evicache.Cache;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class MRUCacheBenchmark {

    private Cache<Integer, Integer> cache;

    @Setup(Level.Iteration)
    public void setup() {
        cache = new MRUCache<>(1000);
        for (int i = 0; i < 1000; i++) {
            cache.put(i, i);
        }
    }

    @Benchmark
    public Integer benchmarkGetOperation() {
        return cache.get(500);
    }

    @Benchmark
    public void benchmarkPutOperation() {
        cache.put(1001, 1001);
    }

    @Benchmark
    public void benchmarkRemoveOperation() {
        cache.remove(300);
    }

}