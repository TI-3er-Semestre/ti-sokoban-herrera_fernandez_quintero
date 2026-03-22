# Test Cases Design - Sokoban Game

**Project:** Sokoban Academic Version  
**Course:** Algorithms and Programming II (APO2)  
**Date:** March 28, 2026  
**Team Members:** [Your names here]

---

## 1. Introduction

This document describes the essential test cases for the Sokoban game model. The tests validate core functional requirements independently of the graphical interface.

### Testing Objectives
- Validate player movement mechanics
- Verify box pushing rules
- Ensure collision detection
- Confirm victory condition
- Test data structures (Stack, Queue, Hash Table, Priority Queue)

---

## 2. Test Cases

### TC-001: Player Movement in Empty Space
**Objective:** Verify player can move in all four directions when space is empty  
**Priority:** Critical

**Test Steps:**
1. Initialize board with player at position (5, 5)
2. Execute moveUp() - verify player at (4, 5)
3. Execute moveRight() - verify player at (4, 6)
4. Execute moveDown() - verify player at (5, 6)
5. Execute moveLeft() - verify player at (5, 5)

**Expected Results:**
- Player position updates correctly for each direction
- Move counter increments by 4
- No exceptions thrown

**Data:**
```
Initial Board (7x7):
# # # # # # #
# . . . . . #
# . . . . . #
# . . P . . #  <- Player at (5,5)
# . . . . . #
# . . . . . #
# # # # # # #
```

---

### TC-002: Player Cannot Move Through Walls
**Objective:** Verify collision detection prevents movement through walls  
**Priority:** Critical

**Test Steps:**
1. Place player at (3, 3) adjacent to wall at (2, 3)
2. Execute moveUp()
3. Verify player position

**Expected Results:**
- Player remains at (3, 3)
- Move counter does NOT increment
- Method returns false

---

### TC-003: Push Box into Empty Space
**Objective:** Verify player can push a box when target space is empty  
**Priority:** Critical

**Test Steps:**
1. Setup: Player at (5, 3), Box at (5, 4), Empty at (5, 5)
2. Execute moveRight()
3. Verify positions

**Expected Results:**
- Player moves to (5, 4)
- Box moves to (5, 5)
- Move counter increments by 1
- Push counter increments by 1

**Data:**
```
Before:        After:
. . . . .      . . . . .
. P B . .  ->  . . P B .
. . . . .      . . . . .
```

---

### TC-004: Cannot Push Box Against Wall
**Objective:** Verify box cannot be pushed if blocked by wall  
**Priority:** Critical

**Test Steps:**
1. Setup: Player at (5, 3), Box at (5, 4), Wall at (5, 5)
2. Execute moveRight()

**Expected Results:**
- Player position unchanged at (5, 3)
- Box position unchanged at (5, 4)
- Move and push counters do NOT increment
- Returns false

---

### TC-005: Cannot Push Two Boxes
**Objective:** Verify player cannot push box if another box is behind it  
**Priority:** Critical

**Test Steps:**
1. Setup: Player at (5, 2), Box1 at (5, 3), Box2 at (5, 4)
2. Execute moveRight()

**Expected Results:**
- All positions remain unchanged
- No counters increment
- Returns false

---

### TC-006: Push Box onto Goal
**Objective:** Verify box can be placed on goal position  
**Priority:** High

**Test Steps:**
1. Setup: Player at (5, 3), Box at (5, 4), Goal at (5, 5)
2. Execute moveRight()
3. Check box status

**Expected Results:**
- Box at position (5, 5) - on goal
- Box state marked as "onGoal"
- Goal visually indicated as occupied

---

### TC-007: Victory with All Boxes on Goals
**Objective:** Verify victory condition when all boxes reach goals  
**Priority:** Critical

**Test Steps:**
1. Setup level with 3 boxes and 3 goals
2. Push first box to goal - check isLevelComplete() returns false
3. Push second box to goal - check isLevelComplete() returns false
4. Push third box to goal - check isLevelComplete() returns true

**Expected Results:**
- Victory only when ALL boxes are on goals
- Game state changes to "won"
- Method isLevelComplete() returns true

---

### TC-008: Load Level from JSON
**Objective:** Verify system parses and loads valid JSON level  
**Priority:** Critical

**Test Data:**
```json
{
  "levelId": 1,
  "name": "Basic Level",
  "width": 7,
  "height": 7,
  "board": [
    "#######",
    "#.....#",
    "#.B.G.#",
    "#..P..#",
    "#.....#",
    "#######"
  ]
}
```

**Test Steps:**
1. Load level from JSON string
2. Verify board dimensions
3. Verify element positions

**Expected Results:**
- Board dimensions: 7x7
- Player at correct position
- Box and goal identified
- Walls positioned correctly

---

### TC-009: Undo Single Move
**Objective:** Verify undo functionality using Stack  
**Priority:** High

**Test Steps:**
1. Player at (5, 5)
2. Move right to (5, 6)
3. Call undo()

**Expected Results:**
- Player returns to (5, 5)
- Move counter decrements by 1
- Stack size decrements
- Game state restored to previous

---

### TC-010: Undo Box Push
**Objective:** Verify undo reverts box push correctly  
**Priority:** High

**Test Steps:**
1. Setup: Player at (5, 3), Box at (5, 4)
2. Push box right
3. Call undo()

**Expected Results:**
- Player back at (5, 3)
- Box back at (5, 4)
- Both move and push counters decrement
- Stack properly manages state

---

### TC-011: Hash Table Detects Duplicate State
**Objective:** Verify transposition table identifies visited states  
**Priority:** High

**Test Steps:**
1. Create game state A
2. Store in hash table
3. Make moves that return to state A
4. Check if state exists in hash table

**Expected Results:**
- Hash function generates consistent hash for same state
- hasVisited() returns true for duplicate state
- Prevents re-exploration in graph search

---

### TC-012: Input Buffer Maintains Command Order
**Objective:** Verify Queue processes commands in FIFO order  
**Priority:** High

**Test Steps:**
1. Enqueue commands: UP, RIGHT, DOWN, LEFT
2. Process queue sequentially

**Expected Results:**
- Commands execute in exact order: UP, RIGHT, DOWN, LEFT
- Queue size decrements correctly
- Player ends at expected position
- No commands lost

---

### TC-013: Leaderboard Top-K Maintenance
**Objective:** Verify Priority Queue maintains only best K scores  
**Priority:** High

**Test Steps:**
1. Set K = 5 for leaderboard
2. Add 10 different scores (varying move counts)
3. Retrieve leaderboard

**Expected Results:**
- Only top 5 scores (lowest move counts) retained
- Scores sorted correctly
- Worst scores automatically removed
- O(log n) insertion time

---

### TC-014: Custom LinkedList Operations
**Objective:** Verify custom LinkedList add and remove  
**Priority:** High

**Test Steps:**
1. Create empty LinkedList
2. Add elements: A, B, C, D, E
3. Remove element C
4. Get element at index 2

**Expected Results:**
- Final list: A, B, D, E
- Size = 4
- Get(2) returns D
- No Java collections used

---

### TC-015: Binary Search Tree Insert and Search
**Objective:** Verify BST maintains ordering property  
**Priority:** Medium

**Test Steps:**
1. Insert: 50, 30, 70, 20, 40, 60, 80
2. Search for 40 (exists)
3. Search for 25 (doesn't exist)
4. In-order traversal

**Expected Results:**
- Search(40) returns true
- Search(25) returns false
- In-order traversal: 20, 30, 40, 50, 60, 70, 80 (sorted)
- BST property maintained (left < root < right)

---

### TC-016: Move and Push Counter Tracking
**Objective:** Verify game statistics are tracked correctly  
**Priority:** Medium

**Test Steps:**
1. Execute 3 regular moves (no boxes)
2. Execute 2 moves that push boxes
3. Execute 1 invalid move (against wall)
4. Get statistics

**Expected Results:**
- Move counter = 5 (only valid moves)
- Push counter = 2
- Invalid move does not increment counters

---

### TC-017: Multiple Undo Operations
**Objective:** Verify Stack handles multiple sequential undos  
**Priority:** Medium

**Test Steps:**
1. Make 5 moves
2. Undo 3 times
3. Make 2 new moves
4. Verify game state

**Expected Results:**
- After 3 undos: game state as if only 2 moves made
- Stack size = 2
- After 2 new moves: stack size = 4
- Total move counter = 4

---

## 3. Traceability Matrix

| Requirement | Test Cases |
|-------------|------------|
| Player Movement | TC-001, TC-002 |
| Box Pushing | TC-003, TC-004, TC-005, TC-006 |
| Victory Condition | TC-007 |
| Level Loading | TC-008 |
| Undo (Stack) | TC-009, TC-010, TC-017 |
| Hash Table | TC-011 |
| Queue | TC-012 |
| Priority Queue | TC-013 |
| LinkedList | TC-014 |
| BST | TC-015 |
| Statistics | TC-016 |

---

## 4. Complexity Analysis Template

For each data structure, document:

### Hash Table (Transposition Table)
- **Insert:** O(1) average, O(n) worst case
- **Search:** O(1) average, O(n) worst case
- **Space:** O(n) where n = number of stored states

### Stack (Undo)
- **Push:** O(1)
- **Pop:** O(1)
- **Space:** O(m) where m = number of moves

### Queue (Input Buffer)
- **Enqueue:** O(1)
- **Dequeue:** O(1)
- **Space:** O(k) where k = buffered commands

### Priority Queue (Leaderboard)
- **Insert:** O(log n)
- **ExtractMin:** O(log n)
- **Space:** O(K) where K = top scores maintained

### LinkedList
- **Add:** O(1) at head/tail, O(n) at index
- **Remove:** O(n)
- **Search:** O(n)

### Binary Search Tree
- **Insert:** O(log n) average, O(n) worst
- **Search:** O(log n) average, O(n) worst
- **Space:** O(n)

---

## 5. Test Execution Notes

- All tests implemented as JUnit test cases
- Tests must be independent (can run in any order)
- Use @Before for setup, @After for cleanup
- Total execution time should be < 3 seconds
- Expected coverage: >80% statement, >75% branch

---

**Document Version:** 1.0  
**Last Updated:** March 28, 2026
