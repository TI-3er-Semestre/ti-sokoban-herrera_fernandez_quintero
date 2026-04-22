package struture;

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

    /**
     * Agrega un elemento al final de la lista
     */
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (tail == null) {
            // Lista vacía: head y tail apuntan al nuevo nodo
            head = newNode;
            tail = newNode;
        } else {
            // Enlazar el último nodo con el nuevo y actualizar tail
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Elimina la primera ocurrencia del elemento en la lista
     * Retorna true si fue eliminado, false si no se encontró
     */
    public boolean remove(T element) {
        if (head == null) return false;

        // Caso especial: el elemento está en el head
        if (head.data.equals(element)) {
            head = head.next;
            if (head == null) tail = null; // lista quedó vacía
            size--;
            return true;
        }

        // Recorrer la lista buscando el elemento
        Node<T> current = head;
        while (current.next != null) {
            if (current.next.data.equals(element)) {
                // Si el nodo a eliminar es el tail, actualizar tail
                if (current.next == tail) {
                    tail = current;
                }
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Retorna el elemento en la posición indicada
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Retorna el tamaño actual de la lista
     */
    public int size() {
        return size;
    }

    /**
     * Retorna true si la lista está vacía
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Retorna true si la lista contiene el elemento
     */
    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Vacía la lista completamente
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
}
