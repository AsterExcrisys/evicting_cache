public class LRUCache {

    private final int[] keys;
    private final int[] values;

    public int[] getKeys() {
        return keys;
    }

    public int[] getValues() {
        return values;
    }

    public LRUCache(int capacity) throws IllegalArgumentException {
        if (capacity < 1) {
            throw new IllegalArgumentException("LRUCache: capacity (int) must be 1 or above");
        }
        keys = new int[capacity];
        values = new int[capacity];
        fillKeys(-1);
        fillValues(-1);
    }

    public int get(int key) {
        int index = searchKey(key);
        if (index >= 0) {
            int value = values[index];
            for (int i = (index - 1); i >= 0; i--) {
                keys[i + 1] = keys[i];
                values[i + 1] = values[i];
            }
            keys[0] = key;
            values[0] = value;
            return value;
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        int index = searchKey(key);
        if (index >= 0) {
            for (int i = (index - 1); i >= 0; i--) {
                keys[i + 1] = keys[i];
                values[i + 1] = values[i];
            }
        } else {
            for (int i = (keys.length - 2); i >= 0; i--) {
                keys[i + 1] = keys[i];
                values[i + 1] = values[i];
            }
        }
        keys[0] = key;
        values[0] = value;
    }

    private void fillKeys(int key) {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = key;
        }
    }

    private void fillValues(int value) {
        for (int i = 0; i < values.length; i++) {
            values[i] = value;
        }
    }

    private int searchKey(int key) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == key) {
                return i;
            }
        }
        return -1;
    }

}