package model;

import struture.CustomLinkedList;
import struture.CustomStack;
import struture.CustomQueue;
import struture.TranspositionTable;

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

    public void loadLevel(Level level) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean move(Direction direction) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean moveUp() {
        return move(Direction.UP);
    }

    public boolean moveDown() {
        return move(Direction.DOWN);
    }

    public boolean moveLeft() {
        return move(Direction.LEFT);
    }

    public boolean moveRight() {
        return move(Direction.RIGHT);
    }

    public boolean isLevelComplete() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean undo() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void resetLevel() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private boolean canPushBox(Box box, Direction direction) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private Box getBoxAt(int row, int col) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean hasBox(int row, int col) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private Position getNextPosition(Position current, Direction direction) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void queueCommand(Direction direction) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void processInputBuffer() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getMoveCount() {
        return moveCount;
    }

    public int getPushCount() {
        return pushCount;
    }

    public long getElapsedTime() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public GameStatus getState() {
        return state;
    }

    public Player getPlayer() {
        return player;
    }

    public CustomLinkedList<Box> getBoxes() {
        return boxes;
    }

    public Board getBoard() {
        return board;
    }
}

