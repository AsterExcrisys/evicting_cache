# 🧠 EvictingCache

A Java-based library that implements multiple **cache eviction strategies**, including LRU, LFU, MRU, MFU, FIFO, and LIFO. Perfect for experimenting with in-memory caching behavior using fixed or variable-capacity caches. Be advised however that this library is in no way ready for production as it is not fully covered by integration and unit tests just yet, so use it at your own discretion.

---

## 📦 Package Structure

```
com.asterexcrisys.evicache
├── fixed                 # Fixed-size cache implementations
│   ├── LRUCache.java
│   ├── LFUCache.java
│   ├── MRUCache.java
│   ├── MFUCache.java
│   ├── FIFOCache.java
│   └── LIFOCache.java
│
├── variable              # Variable-size cache implementations
│   ├── LRUCache.java
│   ├── LFUCache.java
│   ├── MRUCache.java
│   ├── MFUCache.java
│   ├── FIFOCache.java
│   └── LIFOCache.java
│
├── Cache.java            # Interface that any and all caches implement
├── CacheBuilder.java     # Self-explanatory, used to easily build caches with different eviction strategies
└── EvictionPolicy.java   # Enumeration that contains any and all types of eviction strategies
```

---

## 🚀 Getting Started

### 🛠 Requirements
- Java 8 or higher
- IntelliJ IDEA (recommended for project structure)

### 🧪 Example Usage (CacheBuilder)

```java
import com.asterexcrisys.evicache.CacheBuilder;

public class Main {
    
    public static void main(String[] args) {
        Cache<Integer, String> cache = CacheBuilder
                .<Integer, String>newBuilder() // Creates a new builder, Cache<Integer, String> will be its return type in this case
                .evictionPolicy(EvictionPolicy.LRU) // Sets the policy to use when evicting elements from the cache, only applicable to fixed-length caches
                .fixed(true) // Tells the builder that the returned cache should be of the fixed-length version
                .capacity(10) // Sets the initial and, in this case, total capacity to 10
                .build(); // Initializes the cache with the specified parameters

        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        cache.put(4, "four");
        cache.put(5, "five");
        cache.get(1);
        cache.get(1);
        cache.get(2);
        cache.get(3);
        cache.get(2);
        cache.put(6, "six");
        cache.put(7, "seven");
        cache.put(8, "eight");
        cache.put(9, "nine");
        cache.put(10, "ten");
        cache.get(3);
        cache.get(8);
        cache.get(8);
        cache.get(7);
        cache.put(0, "zero"); // Evicts the least recently used element (4)
        cache.get(0);
        
        System.out.println(cache);  // Displays current state of cache through the overridden toString() method
    }
    
}
```

---

## 💡 Eviction Strategies

| Strategy | Description                                                   |
|----------|---------------------------------------------------------------|
| **LRU**  | Least Recently Used - removes the least recently accessed item |
| **LFU**  | Least Frequently Used - removes the least accessed items      |
| **MRU**  | Most Recently Used - removes the most recently accessed item  |
| **MFU**  | Most Frequently Used - removes the most accessed item         |
| **FIFO** | First In, First Out - removes the first inserted item         |
| **LIFO** | Last In, First Out - removes the last inserted item           |

---

## 📈 Future Improvements
- ⏳ Add thread-safe versions
- ⏳ Serialization support
- ⏳ Benchmark performance for each strategy
- ⏳ Add FIFO, LIFO, Time/Expire, and Random eviction strategies

---

## 📄 License

This project is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for more details.

---

## 👨‍💻 Author

Developed by **AsterExcrisys** — feel free to contribute, fork, or suggest features!