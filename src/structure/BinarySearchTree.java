package structure;

public class BinarySearchTree<T extends Comparable<T>> {
    private Node<T> root;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> left;
        Node<T> right;

        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }


    public void insert(T element) {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public boolean search(T element) {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public boolean delete(T element) {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public CustomLinkedList<T> inOrderTraversal() {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public CustomLinkedList<T> preOrderTraversal() {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public CustomLinkedList<T> postOrderTraversal() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public T findMin() {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public T findMax() {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size == 0;
    }


    public void clear() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
