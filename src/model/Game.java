package model;

import structure.CustomLinkedList;
import structure.CustomStack;
import structure.CustomQueue;
import structure.TranspositionTable;

public class Game {
    private Board board;
    private Player player;
    private CustomLinkedList<Box> boxes;
    private Level currentLevel;
    private int moveCount;
    private int pushCount;
    private long startTime;
    private GameStatus state;

    private CustomStack<GameSnapshot> undoStack;
    private CustomQueue<Direction> inputBuffer;
    private TranspositionTable visitedStates;

    public Game() {
        this.boxes = new CustomLinkedList<>();
        this.moveCount = 0;
        this.pushCount = 0;
        this.state = GameStatus.PLAYING;
        this.undoStack = new CustomStack<>();
        this.inputBuffer = new CustomQueue<>();
        this.visitedStates = new TranspositionTable();
    }

    /**
     * Loads a level into the game, resetting all counters and state.
     * A registered player must be set via setPlayer() before or after this call.
     * @pre level != null && level.getBoard() != null && level.getPlayerStartPosition() != null
     * @post board, boxes, counters and timer are reset; state = PLAYING
     */
    public void loadLevel(Level level) {
        this.currentLevel = level;
        this.board = level.getBoard();
        this.moveCount = 0;
        this.pushCount = 0;
        this.startTime = System.currentTimeMillis();
        this.state = GameStatus.PLAYING;
        this.boxes = new CustomLinkedList<>();
        this.undoStack = new CustomStack<>();
        this.visitedStates = new TranspositionTable();

        // If no registered player has been set, create a temporary one
        if (this.player == null) {
            this.player = new Player("Player", "player@game.com", "player", "default", ExperienceLevel.BEGINNER);
        }
        this.player.setPosition(new Position(
                level.getPlayerStartPosition().getRow(),
                level.getPlayerStartPosition().getColumn()
        ));

        // Load boxes from the grid
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                char cell = board.getCell(row, col);
                if (cell == '$' || cell == '*') {
                    Box box = new Box(new Position(row, col));
                    box.setOnGoal(cell == '*');
                    boxes.add(box);
                }
            }
        }
    }

    /**
     * Sets the registered player who will play the level.
     * Call this before loadLevel() or right after.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Moves the player in the given direction.
     * Pushes a box if one is in the way and the space behind it is free.
     * @pre direction != null
     * @return true if the move was valid and executed
     * Time: O(b) where b = number of boxes
     */
    public boolean move(Direction direction) {
        if (state != GameStatus.PLAYING) return false;

        // Save state for undo before attempting move
        undoStack.push(new GameSnapshot(player.getPosition(), boxes, moveCount, pushCount));

        Position currentPos = player.getPosition();
        Position nextPos = getNextPosition(currentPos, direction);

        if (!board.isValidPosition(nextPos.getRow(), nextPos.getColumn())) {
            undoStack.pop(); // discard snapshot since move didn't happen
            return false;
        }
        if (board.isWall(nextPos.getRow(), nextPos.getColumn())) {
            undoStack.pop();
            return false;
        }

        // Check for box in the next position
        if (hasBox(nextPos.getRow(), nextPos.getColumn())) {
            Box box = getBoxAt(nextPos.getRow(), nextPos.getColumn());
            if (!canPushBox(box, direction)) {
                undoStack.pop();
                return false;
            }
            Position boxNextPos = getNextPosition(box.getPosition(), direction);
            box.moveTo(boxNextPos.getRow(), boxNextPos.getColumn());
            box.setOnGoal(board.isGoal(boxNextPos.getRow(), boxNextPos.getColumn()));
            pushCount++;
        }

        player.moveTo(nextPos.getRow(), nextPos.getColumn());
        moveCount++;

        // Register state in transposition table
        visitedStates.put(buildStateKey(), true);

        if (isLevelComplete()) {
            state = GameStatus.WON;
        }

        return true;
    }

    public boolean moveUp()    { return move(Direction.UP); }
    public boolean moveDown()  { return move(Direction.DOWN); }
    public boolean moveLeft()  { return move(Direction.LEFT); }
    public boolean moveRight() { return move(Direction.RIGHT); }

    /**
     * Returns true when every box is positioned on a goal cell.
     * Time: O(b * g) where b = boxes, g = goals
     */
    public boolean isLevelComplete() {
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            if (!board.isGoal(box.getPosition().getRow(), box.getPosition().getColumn())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Undoes the last move, restoring the previous player and box positions.
     * @return true if there was a move to undo
     * Time: O(b)
     */
    public boolean undo() {
        if (undoStack.isEmpty()) return false;
        GameSnapshot snapshot = undoStack.pop();
        player.setPosition(snapshot.getPlayerPosition());
        for (int i = 0; i < boxes.size(); i++) {
            Position savedPos = snapshot.getBoxPositions().get(i);
            boxes.get(i).setPosition(new Position(savedPos.getRow(), savedPos.getColumn()));
            boxes.get(i).setOnGoal(board.isGoal(savedPos.getRow(), savedPos.getColumn()));
        }
        moveCount = snapshot.getMoveCount();
        pushCount = snapshot.getPushCount();
        state = GameStatus.PLAYING;
        return true;
    }

    /** Resets the level to its initial state. */
    public void resetLevel() {
        loadLevel(currentLevel);
    }

    /** Returns elapsed time in seconds since loadLevel() was called. */
    public long getElapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    /**
     * Enqueues a direction command into the input buffer.
     * Prevents losing inputs when user presses keys rapidly.
     */
    public void queueCommand(Direction direction) {
        inputBuffer.enqueue(direction);
    }

    /**
     * Drains and executes all buffered commands in order.
     * Time: O(n * b) where n = commands queued
     */
    public void processInputBuffer() {
        while (!inputBuffer.isEmpty()) {
            move(inputBuffer.dequeue());
        }
    }

    /**
     * Builds a unique string key representing the current game state:
     * player position + all box positions. Used by transposition table.
     * Time: O(b)
     */
    public String buildStateKey() {
        StringBuilder sb = new StringBuilder();
        sb.append(player.getPosition().getRow())
                .append(',')
                .append(player.getPosition().getColumn())
                .append(';');
        for (int i = 0; i < boxes.size(); i++) {
            Position p = boxes.get(i).getPosition();
            sb.append(p.getRow()).append(',').append(p.getColumn()).append('|');
        }
        return sb.toString();
    }

    /** Returns true if the current state was already visited. */
    public boolean isStateVisited() {
        return visitedStates.containsKey(buildStateKey());
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private Position getNextPosition(Position current, Direction direction) {
        switch (direction) {
            case UP:    return new Position(current.getRow() - 1, current.getColumn());
            case DOWN:  return new Position(current.getRow() + 1, current.getColumn());
            case LEFT:  return new Position(current.getRow(), current.getColumn() - 1);
            case RIGHT: return new Position(current.getRow(), current.getColumn() + 1);
            default:    return current;
        }
    }

    public boolean hasBox(int row, int col) {
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            if (box.getPosition().getRow() == row && box.getPosition().getColumn() == col)
                return true;
        }
        return false;
    }

    private Box getBoxAt(int row, int col) {
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            if (box.getPosition().getRow() == row && box.getPosition().getColumn() == col)
                return box;
        }
        return null;
    }

    private boolean canPushBox(Box box, Direction direction) {
        Position nextPos = getNextPosition(box.getPosition(), direction);
        if (!board.isValidPosition(nextPos.getRow(), nextPos.getColumn())) return false;
        if (board.isWall(nextPos.getRow(), nextPos.getColumn())) return false;
        if (hasBox(nextPos.getRow(), nextPos.getColumn())) return false;
        return true;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public int getMoveCount()  { return moveCount; }
    public int getPushCount()  { return pushCount; }
    public GameStatus getState() { return state; }
    public Player getPlayer()  { return player; }
    public CustomLinkedList<Box> getBoxes() { return boxes; }
    public Board getBoard()    { return board; }
    public Level getCurrentLevel() { return currentLevel; }
}
