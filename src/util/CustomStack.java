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

    /**
     * Apila un elemento en la cima de la pila
     */
    public void push(T element) {
        Node<T> newNode = new Node<>(element);
        newNode.next = top;
        top = newNode;
        size++;
    }

    /**
     * Desapila y retorna el elemento en la cima
     * Lanza excepción si la pila está vacía
     */
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía");
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    /**
     * Retorna el elemento en la cima sin desapilarlo
     * Lanza excepción si la pila está vacía
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return top.data;
    }

    /**
     * Retorna true si la pila está vacía
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Retorna el número de elementos en la pila
     */
    public int size() {
        return size;
    }

    /**
     * Vacía la pila completamente
     */
    public void clear() {
        top = null;
        size = 0;
    }
}
