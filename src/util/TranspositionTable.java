package util;

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

    public void put(String key, Object value) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Object get(String key) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean containsKey(String key) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Object remove(String key) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private int hash(String key) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void resize() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
