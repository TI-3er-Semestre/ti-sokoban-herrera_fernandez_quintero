package test;

import structure.CustomStack;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomStackTest {

    @Test
    public void testPushAndPop_RespectsLIFO() {
        CustomStack<Integer> stack = new CustomStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    public void testPop_OnEmptyStack_ThrowsException() {
        CustomStack<Integer> stack = new CustomStack<>();
        assertThrows(IllegalStateException.class, stack::pop);
    }

    @Test
    public void testPeek_DoesNotRemoveElement() {
        CustomStack<Integer> stack = new CustomStack<>();
        stack.push(5);
        assertEquals(5, stack.peek());
        assertEquals(1, stack.size());
    }

    @Test
    public void testSize_AfterMultipleOperations() {
        CustomStack<Integer> stack = new CustomStack<>();
        stack.push(1);
        stack.push(2);
        stack.pop();
        assertEquals(1, stack.size());
    }
}