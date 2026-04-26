package structure;

public class TranspositionTable {
    private static final int DEFAULT_CAPACITY = 101; // Prime number
    private static final double LOAD_FACTOR = 0.75;

    private Entry[] table;
    private int size;
    private int capacity;

    private static class Entry {
        String key;
        Object value;
        Entry next;

        Entry(String key, Object value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    public TranspositionTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Entry[capacity];
        this.size = 0;
    }

    public TranspositionTable(int capacity) {
        this.capacity = capacity;
        this.table = new Entry[capacity];
        this.size = 0;
    }

    /**
     * Inserts or updates a key-value pair using separate chaining.
     * @pre key != null
     * @post key-value stored; size grows by 1 only if key was new
     * Time: O(1) amortized
     */
    public void put(String key, Object value) {
        if ((double) size / capacity >= LOAD_FACTOR) {
            resize();
        }
        int index = hash(key);
        Entry current = table[index];

        // Update existing key if found
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // Insert at front of chain
        Entry newEntry = new Entry(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;
    }

    /**
     * Returns the value for the given key, or null if absent.
     * Time: O(1) amortized
     */
    public Object get(String key) {
        int index = hash(key);
        Entry current = table[index];
        while (current != null) {
            if (current.key.equals(key)) return current.value;
            current = current.next;
        }
        return null;
    }

    /**
     * Returns true if the table contains the given key.
     * Time: O(1) amortized
     */
    public boolean containsKey(String key) {
        int index = hash(key);
        Entry current = table[index];
        while (current != null) {
            if (current.key.equals(key)) return true;
            current = current.next;
        }
        return false;
    }

    /**
     * Removes the entry for the given key.
     * @return removed value, or null if key not found
     * Time: O(1) amortized
     */
    public Object remove(String key) {
        int index = hash(key);
        Entry current = table[index];
        Entry prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) table[index] = current.next;
                else prev.next = current.next;
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    /**
     * Polynomial rolling hash: distributes keys uniformly across buckets.
     * Time: O(k) where k = length of key
     */
    private int hash(String key) {
        int h = 0;
        for (int i = 0; i < key.length(); i++) {
            h = (h * 31 + key.charAt(i)) % capacity;
        }
        return Math.abs(h);
    }

    /**
     * Doubles capacity and rehashes all entries when load factor exceeded.
     * Time: O(n), Space: O(n)
     */
    private void resize() {
        int newCapacity = capacity * 2 + 1;
        Entry[] newTable = new Entry[newCapacity];

        for (int i = 0; i < capacity; i++) {
            Entry current = table[i];
            while (current != null) {
                Entry next = current.next;
                int newIndex = Math.abs(current.key.hashCode() % newCapacity);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }
        table = newTable;
        capacity = newCapacity;
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    /** Clears all buckets. Time: O(capacity), Space: O(1) */
    public void clear() {
        for (int i = 0; i < capacity; i++) table[i] = null;
        size = 0;
    }
}
