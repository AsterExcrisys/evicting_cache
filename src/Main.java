import com.asterexcrisys.evicache.Cache;
import com.asterexcrisys.evicache.CacheBuilder;
import com.asterexcrisys.evicache.EvictionPolicy;

public class Main {

    public static void main(String[] args) {
        Cache<Integer, String> cache = CacheBuilder
                .<Integer, String>newBuilder()
                .evictionPolicy(EvictionPolicy.FIFO)
                .fixedCapacity(true)
                .initialCapacity(10)
                .build();
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        cache.put(4, "four");
        cache.put(5, "five");
        cache.put(6, "six");
        cache.put(7, "seven");
        cache.put(8, "eight");
        cache.put(9, "nine");
        cache.put(10, "ten");
        cache.put(11, "eleven");
        System.out.println(cache);
        cache.get(1);
        cache.get(2);
        cache.get(3);
        cache.get(1);
        cache.get(4);
        cache.get(4);
        cache.put(0, "zero");
        System.out.println(cache);
    }

}