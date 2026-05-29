package com.icesi.sokoban.structure;

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

    /** Inserta un elemento manteniendo la propiedad del min-heap
     * @post el elemento queda ubicado en su posición correcta
     */
    public void insert(T element) {
        if (size == capacity) {
            resize();
        }
        heap[size] = element;
        heapifyUp(size);
        size++;
    }

    /** Elimina y retorna el elemento mínimo
     * @throws IllegalStateException si la cola está vacía
     */
    public T extractMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        T min = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (!isEmpty()) {
            heapifyDown(0);
        }
        return min;
    }

    /** Retorna el mínimo sin eliminarlo
     * @throws IllegalStateException si la cola está vacía
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        return heap[0];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /** Duplica la capacidad del arreglo cuando se llena */
    @SuppressWarnings("unchecked")
    private void resize() {
        capacity = capacity * 2;
        T[] newHeap = (T[]) new Comparable[capacity];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    /** Sube el elemento hacia arriba hasta cumplir la propiedad del heap */
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[index].compareTo(heap[parent]) < 0) {
                T temp = heap[index];
                heap[index] = heap[parent];
                heap[parent] = temp;
                index = parent;
            } else {
                break;
            }
        }
    }

    /** Baja el elemento hacia abajo hasta cumplir la propiedad del heap */
    private void heapifyDown(int index) {
        while (index < size) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && heap[left].compareTo(heap[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && heap[right].compareTo(heap[smallest]) < 0) {
                smallest = right;
            }
            if (smallest != index) {
                T temp = heap[index];
                heap[index] = heap[smallest];
                heap[smallest] = temp;
                index = smallest;
            } else {
                break;
            }
        }
    }
}