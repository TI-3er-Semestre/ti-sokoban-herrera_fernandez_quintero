package util;


public class CustomQueue<T> {
    private Node<T> front;
    private Node<T> rear;
    private int size;


    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public CustomQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void enqueue(T element) {
        // TODO: Implement enqueue
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public T dequeue() {
        // TODO: Implement dequeue
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

    public void clear() {
        // TODO: Implement clear
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
