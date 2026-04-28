package structure;

public class TranspositionTable {
    private static final int DEFAULT_CAPACITY = 101;
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

    // Inserta o actualiza un par clave-valor con encadenamiento separado
    public void put(String key, Object value) {
        if ((double) size / capacity >= LOAD_FACTOR) {
            resize();
        }
        int index = hash(key);
        Entry current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Entry newEntry = new Entry(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;
    }

    // Retorna el valor para una clave, o null si no existe
    public Object get(String key) {
        int index = hash(key);
        Entry current = table[index];
        while (current != null) {
            if (current.key.equals(key)) return current.value;
            current = current.next;
        }
        return null;
    }

    // Verifica si existe la clave
    public boolean containsKey(String key) {
        int index = hash(key);
        Entry current = table[index];
        while (current != null) {
            if (current.key.equals(key)) return true;
            current = current.next;
        }
        return false;
    }

    // Elimina la entrada para la clave dada
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

    // Hash polinomial base 31
    private int hash(String key) {
        int h = 0;
        for (int i = 0; i < key.length(); i++) {
            h = (h * 31 + key.charAt(i)) % capacity;
        }
        return Math.abs(h);
    }

    // Duplica capacidad y rehashea cuando se supera el factor de carga
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

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) table[i] = null;
        size = 0;
    }
}