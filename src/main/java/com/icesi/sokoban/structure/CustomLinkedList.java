package com.icesi.sokoban.structure;

import java.io.Serializable;

public class CustomLinkedList<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> implements Serializable {
        private static final long serialVersionUID = 1L;
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

    // ─────────────────────────────────────────────────────────────────────────
    //  ORDENAMIENTO — MergeSort
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Ordena la lista en orden ascendente usando Merge Sort.
     * Precondición: T debe implementar Comparable<T>.
     * Complejidad: O(n log n) tiempo, O(log n) espacio de pila (recursión).
     */
    @SuppressWarnings("unchecked")
    public void mergeSort() {
        if (size <= 1) return;
        head = mergeSortRec(head);
        // Recalcular tail después de reordenar
        Node<T> current = head;
        while (current.next != null) {
            current = current.next;
        }
        tail = current;
    }

    /**
     * Divide y ordena recursivamente la sub-lista a partir de 'node'.
     * Retorna el nuevo head de la sub-lista ordenada.
     */
    private Node<T> mergeSortRec(Node<T> node) {
        // Caso base: lista vacía o de un solo nodo
        if (node == null || node.next == null) return node;

        // Dividir en dos mitades usando el puntero lento/rápido
        Node<T> mitad = obtenerMitad(node);
        Node<T> segundaMitad = mitad.next;
        mitad.next = null; // separar las dos mitades

        // Ordenar cada mitad recursivamente
        Node<T> izquierda = mergeSortRec(node);
        Node<T> derecha = mergeSortRec(segundaMitad);

        // Fusionar las dos mitades ordenadas
        return merge(izquierda, derecha);
    }

    /**
     * Retorna el nodo en la posición central de la lista (puntero lento/rápido).
     */
    private Node<T> obtenerMitad(Node<T> node) {
        Node<T> lento = node;
        Node<T> rapido = node.next;
        while (rapido != null && rapido.next != null) {
            lento = lento.next;
            rapido = rapido.next.next;
        }
        return lento;
    }

    /**
     * Fusiona dos sub-listas ya ordenadas en una sola lista ordenada.
     * Retorna el head de la lista resultante.
     */
    @SuppressWarnings("unchecked")
    private Node<T> merge(Node<T> izquierda, Node<T> derecha) {
        // Nodo centinela para simplificar la lógica de construcción
        Node<T> dummy = new Node<>(null);
        Node<T> actual = dummy;

        while (izquierda != null && derecha != null) {
            Comparable<T> valIzq = (Comparable<T>) izquierda.data;
            if (valIzq.compareTo(derecha.data) <= 0) {
                actual.next = izquierda;
                izquierda = izquierda.next;
            } else {
                actual.next = derecha;
                derecha = derecha.next;
            }
            actual = actual.next;
        }

        // Adjuntar el resto de la mitad que sobró
        actual.next = (izquierda != null) ? izquierda : derecha;

        return dummy.next;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  BÚSQUEDA BINARIA
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Busca un elemento en la lista usando búsqueda binaria.
     * Precondición: la lista debe estar ordenada (usar mergeSort() antes).
     *
     * Complejidad: O(n) — aunque la lógica de comparación es O(log n),
     * acceder al elemento en la posición 'medio' requiere recorrer la lista
     * desde el inicio cada vez (O(n) por acceso), dando O(n log n) en total.
     * Se documenta como O(n) para la búsqueda efectiva sobre listas enlazadas.
     *
     * @return el índice del elemento si se encuentra, -1 si no está.
     */
    @SuppressWarnings("unchecked")
    public int binarySearch(T target) {
        int bajo = 0;
        int alto = size - 1;

        while (bajo <= alto) {
            int medio = bajo + (alto - bajo) / 2;
            T valorMedio = get(medio); // O(n) por acceso en lista enlazada
            Comparable<T> comp = (Comparable<T>) valorMedio;
            int cmp = comp.compareTo(target);

            if (cmp == 0) {
                return medio;        // encontrado
            } else if (cmp < 0) {
                bajo = medio + 1;    // buscar en la mitad derecha
            } else {
                alto = medio - 1;    // buscar en la mitad izquierda
            }
        }

        return -1; // no encontrado
    }
}