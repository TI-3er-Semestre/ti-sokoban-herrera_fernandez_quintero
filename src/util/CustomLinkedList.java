package util;

public class CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;


    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }


    public CustomLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }


    public void add(T element) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean remove(T element) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public T get(int index) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(T element) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void clear() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
