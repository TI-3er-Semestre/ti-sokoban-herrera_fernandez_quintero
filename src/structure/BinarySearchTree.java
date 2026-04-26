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

    /**
     * Inserts element maintaining BST property.
     * Duplicates are ignored.
     * Time: O(h) where h = tree height; O(log n) average, O(n) worst
     */
    public void insert(T element) {
        root = insertRec(root, element);
    }

    private Node<T> insertRec(Node<T> node, T element) {
        if (node == null) {
            size++;
            return new Node<>(element);
        }
        int cmp = element.compareTo(node.data);
        if (cmp < 0)      node.left  = insertRec(node.left,  element);
        else if (cmp > 0) node.right = insertRec(node.right, element);
        // cmp == 0 → duplicate, ignore
        return node;
    }

    /**
     * Returns true if element exists in the tree.
     * Time: O(h)
     */
    public boolean search(T element) {
        Node<T> current = root;
        while (current != null) {
            int cmp = element.compareTo(current.data);
            if (cmp == 0) return true;
            current = (cmp < 0) ? current.left : current.right;
        }
        return false;
    }

    /**
     * Removes the element from the tree.
     * @return true if element was found and removed
     * Time: O(h)
     */
    public boolean delete(T element) {
        int before = size;
        root = deleteRec(root, element);
        return size < before;
    }

    private Node<T> deleteRec(Node<T> node, T element) {
        if (node == null) return null;
        int cmp = element.compareTo(node.data);
        if (cmp < 0) {
            node.left = deleteRec(node.left, element);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, element);
        } else {
            // Node to delete found
            size--;
            if (node.left == null)  return node.right;
            if (node.right == null) return node.left;
            // Two children: replace with in-order successor (min of right subtree)
            Node<T> successor = findMinNode(node.right);
            node.data = successor.data;
            size++; // deleteRec will decrement again
            node.right = deleteRec(node.right, successor.data);
        }
        return node;
    }

    /**
     * In-order traversal: returns elements in ascending order.
     * Time: O(n), Space: O(n)
     */
    public CustomLinkedList<T> inOrderTraversal() {
        CustomLinkedList<T> result = new CustomLinkedList<>();
        inOrderRec(root, result);
        return result;
    }

    private void inOrderRec(Node<T> node, CustomLinkedList<T> result) {
        if (node == null) return;
        inOrderRec(node.left, result);
        result.add(node.data);
        inOrderRec(node.right, result);
    }

    /**
     * Pre-order traversal: root → left → right.
     * Time: O(n), Space: O(n)
     */
    public CustomLinkedList<T> preOrderTraversal() {
        CustomLinkedList<T> result = new CustomLinkedList<>();
        preOrderRec(root, result);
        return result;
    }

    private void preOrderRec(Node<T> node, CustomLinkedList<T> result) {
        if (node == null) return;
        result.add(node.data);
        preOrderRec(node.left, result);
        preOrderRec(node.right, result);
    }

    /**
     * Post-order traversal: left → right → root.
     * Time: O(n), Space: O(n)
     */
    public CustomLinkedList<T> postOrderTraversal() {
        CustomLinkedList<T> result = new CustomLinkedList<>();
        postOrderRec(root, result);
        return result;
    }

    private void postOrderRec(Node<T> node, CustomLinkedList<T> result) {
        if (node == null) return;
        postOrderRec(node.left, result);
        postOrderRec(node.right, result);
        result.add(node.data);
    }

    /**
     * Returns the minimum element in the tree.
     * Time: O(h)
     */
    public T findMin() {
        if (isEmpty()) throw new IllegalStateException("Tree is empty");
        return findMinNode(root).data;
    }

    private Node<T> findMinNode(Node<T> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /**
     * Returns the maximum element in the tree.
     * Time: O(h)
     */
    public T findMax() {
        if (isEmpty()) throw new IllegalStateException("Tree is empty");
        Node<T> current = root;
        while (current.right != null) current = current.right;
        return current.data;
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    /** Clears the entire tree. Time: O(1), Space: O(1) */
    public void clear() {
        root = null;
        size = 0;
    }
}
