package struture;

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

    // Agrega un elemento al final de la cola
    public void enqueue(T element) {
        Node<T> newNode = new Node<>(element);
        if (rear == null) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    // Elimina y retorna el elemento al frente de la cola
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("La cola está vacía");
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    // Retorna el elemento al frente sin eliminarlo
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("La cola está vacía");
        }
        return front.data;
    }

    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
