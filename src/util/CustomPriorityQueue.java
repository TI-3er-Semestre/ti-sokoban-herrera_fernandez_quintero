package util;

public class CustomPriorityQueue<T extends Comparable<T>> {
    private T[] heap;
    private int size;
    private int capacity;


    @SuppressWarnings("unchecked")
    public CustomPriorityQueue() {
        this.capacity = 10;
        this.heap = (T[]) new Comparable[capacity];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public CustomPriorityQueue(int capacity) {
        this.capacity = capacity;
        this.heap = (T[]) new Comparable[capacity];
        this.size = 0;
    }

    public void insert(T element) {
        // TODO: Implement insert with heapify up
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public T extractMin() {
        // TODO: Implement extract min with heapify down
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public T peek() {
        // TODO: Implement peek
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        // TODO: Implement array resize
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void heapifyUp(int index) {
        // TODO: Implement heapify up
        throw new UnsupportedOperationException("Not implemented yet");
    }


    private void heapifyDown(int index) {
        // TODO: Implement heapify down
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
