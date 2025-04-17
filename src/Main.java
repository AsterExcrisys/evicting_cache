import com.asterexcrisys.evicache.fixed.LRUCache;

public class Main {

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(10);
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        cache.get(1);
        cache.put(4, "four");
        cache.put(5, "five");
        System.out.println(cache);
    }

}