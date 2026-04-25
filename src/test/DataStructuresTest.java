package test;

import util.*;
import model.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DataStructuresTest {

    @Test
    public void testHashTable_DetectsDuplicateState() {
        fail("Test not implemented yet");
    }

    @Test
    public void testQueue_MaintainsCommandOrder() {
        CustomQueue<Direction> queue = new CustomQueue<>();
        queue.enqueue(Direction.UP);
        queue.enqueue(Direction.RIGHT);
        queue.enqueue(Direction.DOWN);
        queue.enqueue(Direction.LEFT);

        assertEquals(Direction.UP, queue.dequeue());
        assertEquals(Direction.RIGHT, queue.dequeue());
        assertEquals(Direction.DOWN, queue.dequeue());
        assertEquals(Direction.LEFT, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testPriorityQueue_TopKMaintenance() {
        CustomPriorityQueue<Integer> pq = new CustomPriorityQueue<>();
        int[] values = {40, 15, 62, 28, 9, 51, 33, 47};
        for (int v : values) {
            pq.insert(v);
        }
        int prev = pq.extractMin();
        while (!pq.isEmpty()) {
            int current = pq.extractMin();
            assertTrue(prev <= current);
            prev = current;
        }
    }

    @Test
    public void testLinkedList_AddAndRemove() {
        fail("Test not implemented yet");
    }

    @Test
    public void testBST_InsertAndSearch() {
        fail("Test not implemented yet");
    }
}
