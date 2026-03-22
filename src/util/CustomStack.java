package util;

public class CustomStack<T> {
    private Node<T> top;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public CustomStack() {
        this.top = null;
        this.size = 0;
    }

    public void push(T element) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public T pop() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public T peek() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
