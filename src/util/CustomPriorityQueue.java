package util;

public class CustomPriorityQueue<T extends Comparable<T>> {
    private Node<T> head;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    /** Inserta un elemento manteniendo el orden ascendente
     * @post el elemento queda ubicado de forma que head siempre tiene el mínimo
     */
    public void insert(T element) {
        Node<T> newNode = new Node<>(element);

        // Caso 1: lista vacía o el nuevo es menor que el head
        if (head == null || element.compareTo(head.data) < 0) {
            newNode.next = head;
            head = newNode;
        } else {
            // Caso 2: buscar la posición correcta recorriendo la lista
            Node<T> current = head;
            while (current.next != null && current.next.data.compareTo(element) < 0) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    /** Elimina y retorna el elemento mínimo (el del frente)
     * @throws IllegalStateException si la cola está vacía
     */
    public T extractMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    /** Retorna el mínimo sin eliminarlo
     * @throws IllegalStateException si la cola está vacía
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        return head.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /** Vacía la cola completamente
     */
    public void clear() {
        head = null;
        size = 0;
    }
}
