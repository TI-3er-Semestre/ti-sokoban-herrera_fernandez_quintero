package com.icesi.sokoban.structure;

import com.icesi.sokoban.structure.*;
import com.icesi.sokoban.model.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DataStructuresTest {

    // ── TranspositionTable ────────────────────────────────────────────────────

    @Test
    public void testHashTable_DetectsDuplicateState() {
        TranspositionTable table = new TranspositionTable();
        String stateKey = "3,2;4,5|6,1|";
        assertFalse(table.containsKey(stateKey));
        table.put(stateKey, true);
        assertTrue(table.containsKey(stateKey));
    }

    @Test
    public void testHashTable_PutAndGet_ReturnsCorrectValue() {
        TranspositionTable table = new TranspositionTable();
        table.put("key1", 42);
        assertEquals(42, table.get("key1"));
    }

    @Test
    public void testHashTable_UpdateExistingKey() {
        TranspositionTable table = new TranspositionTable();
        table.put("state", "first");
        table.put("state", "second");
        assertEquals("second", table.get("state"));
        assertEquals(1, table.size());
    }

    @Test
    public void testHashTable_Remove_DecreasesSize() {
        TranspositionTable table = new TranspositionTable();
        table.put("k1", 1);
        table.put("k2", 2);
        table.remove("k1");
        assertFalse(table.containsKey("k1"));
        assertEquals(1, table.size());
    }

    @Test
    public void testHashTable_Clear_EmptiesTable() {
        TranspositionTable table = new TranspositionTable();
        table.put("a", 1);
        table.put("b", 2);
        table.clear();
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
    }

    @Test
    public void testHashTable_ManyEntries_HandlesCollisions() {
        TranspositionTable table = new TranspositionTable(5);
        for (int i = 0; i < 20; i++) {
            table.put("key" + i, i);
        }
        for (int i = 0; i < 20; i++) {
            assertEquals(i, table.get("key" + i));
        }
    }

    // ── CustomQueue ───────────────────────────────────────────────────────────

    @Test
    public void testQueue_MaintainsCommandOrder() {
        CustomQueue<Direction> queue = new CustomQueue<>();
        queue.enqueue(Direction.UP);
        queue.enqueue(Direction.RIGHT);
        queue.enqueue(Direction.DOWN);
        queue.enqueue(Direction.LEFT);

        assertEquals(Direction.UP,    queue.dequeue());
        assertEquals(Direction.RIGHT, queue.dequeue());
        assertEquals(Direction.DOWN,  queue.dequeue());
        assertEquals(Direction.LEFT,  queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testQueue_Peek_DoesNotRemove() {
        CustomQueue<Integer> queue = new CustomQueue<>();
        queue.enqueue(10);
        queue.enqueue(20);
        assertEquals(10, queue.peek());
        assertEquals(2, queue.size());
    }

    @Test
    public void testQueue_DequeueOnEmpty_ThrowsException() {
        CustomQueue<String> queue = new CustomQueue<>();
        assertThrows(IllegalStateException.class, queue::dequeue);
    }

    @Test
    public void testQueue_Clear_EmptiesQueue() {
        CustomQueue<Integer> queue = new CustomQueue<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.clear();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    // ── CustomPriorityQueue ───────────────────────────────────────────────────

    @Test
    public void testPriorityQueue_TopKMaintenance() {
        CustomPriorityQueue<Integer> pq = new CustomPriorityQueue<>();
        int[] values = {40, 15, 62, 28, 9, 51, 33, 47};
        for (int v : values) pq.insert(v);

        int prev = pq.extractMin();
        while (!pq.isEmpty()) {
            int current = pq.extractMin();
            assertTrue(prev <= current);
            prev = current;
        }
    }

    @Test
    public void testPriorityQueue_ExtractMinOnEmpty_ThrowsException() {
        CustomPriorityQueue<Integer> pq = new CustomPriorityQueue<>();
        assertThrows(IllegalStateException.class, pq::extractMin);
    }

    @Test
    public void testPriorityQueue_Peek_DoesNotRemove() {
        CustomPriorityQueue<Integer> pq = new CustomPriorityQueue<>();
        pq.insert(5);
        pq.insert(3);
        assertEquals(3, pq.peek());
        assertEquals(2, pq.size());
    }

    // ── CustomLinkedList ──────────────────────────────────────────────────────

    @Test
    public void testLinkedList_AddAndRemove() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        assertEquals(3, list.size());
        assertTrue(list.remove("b"));
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
        assertEquals("c", list.get(1));
    }

    @Test
    public void testLinkedList_Contains() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(10);
        list.add(20);
        assertTrue(list.contains(10));
        assertFalse(list.contains(99));
    }

    @Test
    public void testLinkedList_GetOutOfBounds_ThrowsException() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
    }

    @Test
    public void testLinkedList_Clear() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(1);
        list.add(2);
        list.clear();
        assertTrue(list.isEmpty());
    }

    // ── BinarySearchTree ──────────────────────────────────────────────────────

    @Test
    public void testBST_InsertAndSearch() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);
        bst.insert(40);
        assertTrue(bst.search(30));
        assertTrue(bst.search(70));
        assertFalse(bst.search(99));
    }

    @Test
    public void testBST_InOrderTraversal_ReturnsSortedList() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) bst.insert(v);

        CustomLinkedList<Integer> inOrder = bst.inOrderTraversal();
        for (int i = 0; i < inOrder.size() - 1; i++) {
            assertTrue(inOrder.get(i) <= inOrder.get(i + 1));
        }
    }

    @Test
    public void testBST_FindMinAndMax() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(50);
        bst.insert(10);
        bst.insert(90);
        bst.insert(30);
        assertEquals(10, bst.findMin());
        assertEquals(90, bst.findMax());
    }

    @Test
    public void testBST_Delete_RemovesElement() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        assertTrue(bst.delete(30));
        assertFalse(bst.search(30));
        assertEquals(2, bst.size());
    }

    @Test
    public void testBST_DuplicatesIgnored() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(10);
        bst.insert(10);
        assertEquals(1, bst.size());
    }
}