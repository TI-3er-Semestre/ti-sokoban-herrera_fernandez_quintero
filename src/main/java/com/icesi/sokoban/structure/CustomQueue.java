package com.icesi.sokoban.structure;

import java.io.Serializable;

public class CustomQueue<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Node<T> front;
    private Node<T> rear;
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

    public CustomQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /** Agrega un elemento al final de la cola
    @post aumenta el tamaño en 1, rear apunta al nuevo nodo
    **/
    public void enqueue(T element) {
        Node<T> newNode = new Node<>(element);

        if (rear == null){
            front = newNode;
            rear = newNode;

        } else {

            rear.next = newNode;
            rear = newNode;
        }

        size++;
    }

    /** Elimina y retorna el elemento que esta en el frente
     @throws IllegalStateException si la cola esta vacia
     **/
    public T dequeue() {
        if (isEmpty()){
            throw new IllegalStateException("Queue is empty");
        }
        T data = front.data;
        front = front.next;
        if (front == null){
            rear = null;
        }
        size--;
        return data;
    }

    /** Retorna el elemento del frente sin eliminarlo
     * @throws IllegalStateException si la cola esta vacia
     * @return
     */
    public T peek() {
        if (isEmpty()){
            throw new IllegalStateException("Queue is empty");
        }
        return front.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /** Vacia la cola completamente
     */
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }
}
