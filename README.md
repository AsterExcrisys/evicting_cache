# 🧠 EvictingCache

A Java-based library that implements multiple **cache eviction strategies**, including LRU, LFU, MRU, MFU, FIFO, and LIFO. Perfect for experimenting with in-memory caching behavior using fixed or variable-capacity caches. However, since most implementations internally use an array, they are not fit for storing large quantities of data; if that is your case, I would suggest you to use a library like [Guava](https://www.github.com/google/guava) or other any heap-based caches.

Be advised that this library is in no way ready for production as it is not fully covered by integration and unit tests just yet, so use it at your own discretion.

if you would like to know more about cache eviction policies and which one best fits your case, you can start by reading this [article](https://en.wikipedia.org/wiki/Cache_replacement_policies).

---

## 📦 Package Structure

```
com.asterexcrisys.evicache
│
├── access              # Access-based cache implementations                      
│   ├── fixed                   # Fixed-size versions
│   │   ├── LRUCache.java
│   │   └── MRUCache.java
│   │
│   └── variable                # Variable-size versions
│       ├── LRUCache.java
│       └── MRUCache.java
│
├── frequency           # Frequency-based cache implementations
│   ├── fixed                   # Fixed-size versions
│   │   ├── LFUCache.java
│   │   └── MFUCache.java
│   │
│   └── variable                # Variable-size versions
│       ├── LFUCache.java
│       └── MFUCache.java
│
├── order               # Order-based cache implementations
│   ├── fixed                   # Fixed-size versions
│   │   ├── FIFOCache.java
│   │   └── LIFOCache.java
│   │
│   └── variable                # Variable-size versions
│       ├── FIFOCache.java
│       └── LIFOCache.java
│
├── time                # Time-based cache implementations
│   ├── fixed                   # Fixed-size versions
│   │   ├── TimeCache.java
│   │   └── ExpireCache.java
│   │
│   └── variable                # Variable-size versions
│       ├── TimeCache.java
│       └── ExpireCache.java
│
├── extra               # Extra cache implementations (e.g priority-based or random-based)
│   ├── fixed                   # Fixed-size versions
│   │   ├── PriorityCache.java
│   │   └── RandomCache.java
│   │
│   └── variable                # Variable-size versions
│       ├── PriorityCache.java
│       └── RandomCache.java
│
├── Cache.java              # Interface that any and all caches implement
├── CacheEntry.java         # Interface that any and all cache entries implement
├── BasicCacheEntry.java    # Cache entry used by most implementations (has only the basic 'key' and 'value' fields)
├── PriorityCacheEntry.java # Cache entry used only by PriorityCache (has one additional 'priority' field)
├── ExpireCacheEntry.java   # Cache entry used only by ExpireCache (has two additional 'time' and 'unit' fields)
├── CacheBuilder.java       # Self-explanatory, used to easily build caches with different eviction strategies
├── CacheRecorder.java      # Self-explanatory, used to record core metrics of any type of cache
├── EvictionPolicy.java     # Enumeration that contains any and all types of eviction strategies
└── MetricType.java         # Enumeration that contains any and all types of metrics recorded by CacheRecorder
```

---

## 🧰 Technologies Used

| Category        | Tool / Technology              | Notes                                         |
|-----------------|--------------------------------|-----------------------------------------------|
| Language        | Java 17                        | Modern LTS version                            |
| Build Tool      | Maven                          | With annotation processing + JMH support      |
| Testing         | JUnit 5                        | For unit and integration testing              |
| Benchmarking    | JMH                            | For precise micro-benchmarking                |
| Documentation   | Javadoc and GitHub Wikis       | Interface and API documentation               |
| Caching Design  | Custom `Cache<K, V>` interface | Supports top/bottom access and eviction logic |
| IDE             | IntelliJ IDEA                  | With annotation processing enabled            |

---

## 🚀 Getting Started

### 🛠 Requirements
- Java 17 or higher
- Maven (recommended as the project build system)
- IntelliJ IDEA (recommended for project structure)

### 🧪 Example Usage (CacheBuilder)

```java
import com.asterexcrisys.evicache.BasicCacheEntry;
import com.asterexcrisys.evicache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        Cache<Integer, String> cache = CacheBuilder
                .<Integer, String>newBuilder() // Creates a new builder, Cache<Integer, String> will be its return type in this case
                .evictionPolicy(EvictionPolicy.LRU) // Sets the policy to use when evicting elements from the cache, only applicable to fixed-length caches
                .expireTime(1, TimeUnit.MINUTES) // Sets the expiry time of all elements to 1 minute, only applicable to time-based caches
                .fixedCapacity(true) // Tells the builder that the returned cache should be of the fixed-length version
                .initialCapacity(10) // Sets the initial and, in this case, total capacity to 10
                .build(); // Initializes the cache with the specified parameters

        cache.put(new BasicCacheEntry<>(1, "one"));
        cache.put(new BasicCacheEntry<>(2, "two"));
        cache.put(new BasicCacheEntry<>(3, "three"));
        cache.put(new BasicCacheEntry<>(4, "four"));
        cache.put(new BasicCacheEntry<>(5, "five"));
        cache.get(1);
        cache.get(1);
        cache.get(2);
        cache.get(3);
        cache.get(2);
        cache.put(new BasicCacheEntry<>(6, "six"));
        cache.put(new BasicCacheEntry<>(7, "seven"));
        cache.put(new BasicCacheEntry<>(8, "eight"));
        cache.put(new BasicCacheEntry<>(9, "nine"));
        cache.put(new BasicCacheEntry<>(10, "ten"));
        cache.get(3);
        cache.get(8);
        cache.get(8);
        cache.get(7);
        cache.put(new BasicCacheEntry<>(0, "zero")); // Evicts the least recently used element (4)
        cache.get(0);

        System.out.println(cache);  // Displays current state of cache through the overridden toString() method
    }

}
```

---

## 💡 Eviction Strategies

| Strategy     | Description                                                                                                                                                                           |
|--------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **LRU**      | Least Recently Used - removes the least recently accessed item                                                                                                                        |
| **LFU**      | Least Frequently Used - removes the least accessed item                                                                                                                               |
| **MRU**      | Most Recently Used - removes the most recently accessed item                                                                                                                          |
| **MFU**      | Most Frequently Used - removes the most accessed item                                                                                                                                 |
| **FIFO**     | First In, First Out - removes the first inserted item                                                                                                                                 |
| **LIFO**     | Last In, First Out - removes the last inserted item                                                                                                                                   |
| **Time**     | Time - lets you define a global expiry time (applies to all),<br/> after which elements will become inaccesible and<br/>removed lazily - removes the first inserted item              |
| **Expire**   | Expire - lets you define an expiry time for each element,<br/>after which only that element will become inaccesible and<br/>removed lazily - removes the item that is first to expire |
| **Priority** | Priority - removes the item with the least priority value                                                                                                                             |
| **Random**   | Random - removes an item at a random index/position                                                                                                                                   |

---

## 📈 Future Improvements
- ✅ Provide extensive documentation (both via Javadoc and GitHub Wikis)
- ⏳ Add thread-safe versions
- ⏳ Add serialization support
- ⏳ Add iterator support
- ⏳ Add benchmark performance for each strategy
- ⏳ Add FIFO, LIFO, Time/Expire, and Random eviction strategies

---

## 🔍 Considerations

Considering the results of the benchmarks performed on access-based and frequency-based caches, reporting that the `remove` operation is by far the most time-consuming, I have come to the conclusion that such is because of it immediately nullifying the elements removed and thus indirectly calling the garbage collector to free up the unreferenced memory addresses, for this reason I am taking into account the possibility of implementing a `lazy remove` instead.

A `lazy remove` will leave the elements referenced in the array and just reduce the size, so that they become 'virtually inaccesible'. The actual removal will happen when they are overwritten by new elements or evicted completely from the cache.

---

## 📄 License

This project is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for more details.

---

## 👨‍💻 Author

Developed by [AsterExcrisys](https://www.github.com/AsterExcrisys) — feel free to contribute, fork, or suggest features!