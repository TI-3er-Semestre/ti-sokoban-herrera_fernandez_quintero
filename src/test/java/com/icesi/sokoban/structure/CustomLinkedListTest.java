package com.icesi.sokoban.structure;

import com.icesi.sokoban.structure.CustomLinkedList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CustomLinkedListTest {

    private CustomLinkedList<Integer> list;

    @BeforeEach
    public void setUp() {
        list = new CustomLinkedList<>();
    }

    @Test
    public void testAdd_SingleElement_SizeIsOne() {
        list.add(42);
        assertEquals(1, list.size());
        assertEquals(42, list.get(0));
    }

    @Test
    public void testAdd_MultipleElements_MaintainsOrder() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void testRemove_ExistingElement_ReturnsTrue() {
        list.add(10);
        list.add(20);
        list.add(30);
        assertTrue(list.remove(20));
        assertEquals(2, list.size());
        assertEquals(30, list.get(1));
    }

    @Test
    public void testRemove_HeadElement_UpdatesHead() {
        list.add(5);
        list.add(10);
        assertTrue(list.remove(5));
        assertEquals(10, list.get(0));
        assertEquals(1, list.size());
    }

    @Test
    public void testRemove_TailElement_UpdatesTail() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertTrue(list.remove(3));
        assertEquals(2, list.size());
        assertEquals(2, list.get(list.size() - 1));
    }

    @Test
    public void testRemove_NonExistingElement_ReturnsFalse() {
        list.add(1);
        assertFalse(list.remove(99));
        assertEquals(1, list.size());
    }

    @Test
    public void testGet_InvalidIndex_ThrowsException() {
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    public void testIsEmpty_NewList_ReturnsTrue() {
        assertTrue(list.isEmpty());
    }

    @Test
    public void testIsEmpty_AfterAdd_ReturnsFalse() {
        list.add(1);
        assertFalse(list.isEmpty());
    }

    @Test
    public void testContains_ExistingElement_ReturnsTrue() {
        list.add(100);
        list.add(200);
        assertTrue(list.contains(100));
        assertTrue(list.contains(200));
    }

    @Test
    public void testContains_NonExistingElement_ReturnsFalse() {
        list.add(1);
        assertFalse(list.contains(999));
    }

    @Test
    public void testClear_RemovesAllElements() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    public void testAdd_AfterClear_WorksCorrectly() {
        list.add(1);
        list.clear();
        list.add(99);
        assertEquals(1, list.size());
        assertEquals(99, list.get(0));
    }

    @Test
    public void testRemove_OnEmptyList_ReturnsFalse() {
        assertFalse(list.remove(1));
    }

    @Test
    public void testSize_IncrementsAndDecrements() {
        assertEquals(0, list.size());
        list.add(1);
        list.add(2);
        assertEquals(2, list.size());
        list.remove(1);
        assertEquals(1, list.size());
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  MergeSort
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    public void testMergeSort_UnsortedList_ReturnsSortedAscending() {
        list.add(5);
        list.add(2);
        list.add(8);
        list.add(1);
        list.add(3);
        list.mergeSort();
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(5, list.get(3));
        assertEquals(8, list.get(4));
    }

    @Test
    public void testMergeSort_AlreadySortedList_RemainsCorrect() {
        list.add(10);
        list.add(20);
        list.add(30);
        list.mergeSort();
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    public void testMergeSort_ReversedList_SortsCorrectly() {
        list.add(9);
        list.add(7);
        list.add(5);
        list.add(3);
        list.add(1);
        list.mergeSort();
        assertEquals(1, list.get(0));
        assertEquals(9, list.get(4));
    }

    @Test
    public void testMergeSort_SingleElement_NoChange() {
        list.add(42);
        list.mergeSort();
        assertEquals(1, list.size());
        assertEquals(42, list.get(0));
    }

    @Test
    public void testMergeSort_EmptyList_NoException() {
        assertDoesNotThrow(() -> list.mergeSort());
        assertEquals(0, list.size());
    }

    @Test
    public void testMergeSort_DuplicateElements_PreservesAllElements() {
        list.add(3);
        list.add(1);
        list.add(3);
        list.add(2);
        list.mergeSort();
        assertEquals(4, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(3, list.get(3));
    }

    @Test
    public void testMergeSort_TwoElements_SortsCorrectly() {
        list.add(10);
        list.add(3);
        list.mergeSort();
        assertEquals(3, list.get(0));
        assertEquals(10, list.get(1));
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  BinarySearch
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    public void testBinarySearch_ExistingElement_ReturnsCorrectIndex() {
        list.add(1);
        list.add(3);
        list.add(5);
        list.add(7);
        list.add(9);
        // lista ya ordenada
        assertEquals(2, list.binarySearch(5));
    }

    @Test
    public void testBinarySearch_FirstElement_ReturnsZero() {
        list.add(10);
        list.add(20);
        list.add(30);
        assertEquals(0, list.binarySearch(10));
    }

    @Test
    public void testBinarySearch_LastElement_ReturnsLastIndex() {
        list.add(10);
        list.add(20);
        list.add(30);
        assertEquals(2, list.binarySearch(30));
    }

    @Test
    public void testBinarySearch_NonExistingElement_ReturnsMinusOne() {
        list.add(2);
        list.add(4);
        list.add(6);
        assertEquals(-1, list.binarySearch(5));
    }

    @Test
    public void testBinarySearch_EmptyList_ReturnsMinusOne() {
        assertEquals(-1, list.binarySearch(1));
    }

    @Test
    public void testBinarySearch_AfterMergeSort_FindsElement() {
        list.add(15);
        list.add(3);
        list.add(9);
        list.add(1);
        list.add(7);
        list.mergeSort(); // [1, 3, 7, 9, 15]
        int index = list.binarySearch(9);
        assertEquals(9, list.get(index));
    }

    @Test
    public void testBinarySearch_SingleElement_Found() {
        list.add(42);
        assertEquals(0, list.binarySearch(42));
    }

    @Test
    public void testBinarySearch_SingleElement_NotFound() {
        list.add(42);
        assertEquals(-1, list.binarySearch(99));
    }
}