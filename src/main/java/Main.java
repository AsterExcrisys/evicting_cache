import com.asterexcrisys.evicache.entries.BasicCacheEntry;
import com.asterexcrisys.evicache.Cache;
import com.asterexcrisys.evicache.CacheBuilder;
import com.asterexcrisys.evicache.models.EvictionPolicy;
import java.util.concurrent.TimeUnit;

public class Main {

    // TODO: read more about S3-FIFO at https://github.com/Thesys-lab/sosp23-s3fifo

    public static void main(String[] args) {
        Cache<Integer, String> cache = CacheBuilder
                .<Integer, String>newBuilder()
                .evictionPolicy(EvictionPolicy.RANDOM)
                .expireTime(1, TimeUnit.MINUTES)
                .fixedCapacity(true)
                .initialCapacity(10)
                .build();
        cache.put(new BasicCacheEntry<>(1, "one"));
        cache.put(new BasicCacheEntry<>(2, "two"));
        cache.put(new BasicCacheEntry<>(3, "three"));
        cache.put(new BasicCacheEntry<>(4, "four"));
        cache.put(new BasicCacheEntry<>(5, "five"));
        cache.put(new BasicCacheEntry<>(6, "six"));
        cache.put(new BasicCacheEntry<>(7, "seven"));
        cache.put(new BasicCacheEntry<>(8, "eight"));
        cache.put(new BasicCacheEntry<>(9, "nine"));
        cache.put(new BasicCacheEntry<>(10, "ten"));
        System.out.println(cache);
        cache.put(new BasicCacheEntry<>(11, "eleven"));
        cache.put(new BasicCacheEntry<>(12, "twelve"));
        System.out.println(cache);
        for (int i = 0; i < 10; i++) {
            System.out.println(cache.popTop());
        }
        System.out.println(cache);
    }

}