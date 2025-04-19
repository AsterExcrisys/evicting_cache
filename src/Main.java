import com.asterexcrisys.evicache.fixed.LFUCache;

public class Main {

    public static void main(String[] args) {
        LFUCache<Integer, String> cache = new LFUCache<>(10);
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        cache.put(4, "four");
        cache.put(5, "five");
        cache.get(1);
        cache.put(6, "six");
        cache.put(7, "seven");
        cache.get(2);
        cache.get(1);
        cache.get(3);
        cache.get(2);
        cache.get(1);
        System.out.println(cache);
    }

}