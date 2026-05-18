package com.icesi.sokoban.model;

import com.icesi.sokoban.structure.CustomLinkedList;
import com.icesi.sokoban.structure.CustomStack;
import com.icesi.sokoban.structure.CustomQueue;
import com.icesi.sokoban.structure.TranspositionTable;

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

    // Carga un nivel en el juego
    public void loadLevel(Level level) {
        this.currentLevel = level;
        this.board = level.getBoard();
        this.moveCount = 0;
        this.pushCount = 0;
        this.startTime = System.currentTimeMillis();
        this.state = GameStatus.PLAYING;
        this.boxes = new CustomLinkedList<>();
        this.undoStack = new CustomStack<>();

        // Crear el jugador en la posición inicial
        this.player = new Player(
                level.getPlayerStartPosition().getRow() + "",
                "", "", "", null
        );
        this.player.setPosition(level.getPlayerStartPosition());

        // Cargar las cajas desde el grid
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (board.getCell(row, col) == '$' || board.getCell(row, col) == '*') {
                    boxes.add(new Box(new Position(row, col)));
                }
            }
        }
    }

    // Mueve el jugador en una dirección
    public boolean move(Direction direction) {
        if (state != GameStatus.PLAYING) return false;

        // Guardar estado antes de mover para el undo
        undoStack.push(new GameSnapshot(player.getPosition(), boxes, moveCount, pushCount));

        Position currentPos = player.getPosition();
        Position nextPos = getNextPosition(currentPos, direction);

        // Verificar que la siguiente posición es válida
        if (!board.isValidPosition(nextPos.getRow(), nextPos.getColumn())) return false;
        if (board.isWall(nextPos.getRow(), nextPos.getColumn())) return false;

        // Verificar si hay una caja
        if (hasBox(nextPos.getRow(), nextPos.getColumn())) {
            Box box = getBoxAt(nextPos.getRow(), nextPos.getColumn());

            // Verificar si la caja puede ser empujada
            if (!canPushBox(box, direction)) return false;

            // Mover la caja
            Position boxNextPos = getNextPosition(box.getPosition(), direction);
            box.moveTo(boxNextPos.getRow(), boxNextPos.getColumn());
            box.setOnGoal(board.isGoal(boxNextPos.getRow(), boxNextPos.getColumn()));
            pushCount++;
        }

        // Mover el jugador
        player.moveTo(nextPos.getRow(), nextPos.getColumn());
        moveCount++;

        // Verificar victoria
        if (isLevelComplete()) {
            state = GameStatus.WON;
        }

        return true;
    }

    public boolean moveUp() { return move(Direction.UP); }
    public boolean moveDown() { return move(Direction.DOWN); }
    public boolean moveLeft() { return move(Direction.LEFT); }
    public boolean moveRight() { return move(Direction.RIGHT); }

    // Verifica si el nivel está completo
    public boolean isLevelComplete() {
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            if (!board.isGoal(box.getPosition().getRow(), box.getPosition().getColumn())) {
                return false;
            }
        }
        return true;
    }

    // Deshace el último movimiento
    public boolean undo() {
        if (undoStack.isEmpty()) return false;
        GameSnapshot snapshot = undoStack.pop();
        player.setPosition(snapshot.getPlayerPosition());
        for (int i = 0; i < boxes.size(); i++) {
            boxes.get(i).setPosition(snapshot.getBoxPositions().get(i));
            boxes.get(i).setOnGoal(board.isGoal(
                    snapshot.getBoxPositions().get(i).getRow(),
                    snapshot.getBoxPositions().get(i).getColumn()));
        }
        moveCount = snapshot.getMoveCount();
        pushCount = snapshot.getPushCount();
        state = GameStatus.PLAYING;
        return true;
    }

    // Reinicia el nivel al estado inicial
    public void resetLevel() {
        loadLevel(currentLevel);
    }

    // Retorna el tiempo transcurrido en segundos
    public long getElapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    // Agrega un comando a la cola de entrada
    public void queueCommand(Direction direction) {
        inputBuffer.enqueue(direction);
    }

    // Procesa los comandos de la cola de entrada
    public void processInputBuffer() {
        while (!inputBuffer.isEmpty()) {
            Direction direction = inputBuffer.dequeue();
            move(direction);
        }
    }

    // Calcula la siguiente posición según la dirección
    private Position getNextPosition(Position current, Direction direction) {
        switch (direction) {
            case UP:    return new Position(current.getRow() - 1, current.getColumn());
            case DOWN:  return new Position(current.getRow() + 1, current.getColumn());
            case LEFT:  return new Position(current.getRow(), current.getColumn() - 1);
            case RIGHT: return new Position(current.getRow(), current.getColumn() + 1);
            default:    return current;
        }
    }

    // Verifica si hay una caja en una posición
    public boolean hasBox(int row, int col) {
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            if (box.getPosition().getRow() == row && box.getPosition().getColumn() == col) {
                return true;
            }
        }
        return false;
    }

    // Retorna la caja en una posición, null si no hay
    private Box getBoxAt(int row, int col) {
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            if (box.getPosition().getRow() == row && box.getPosition().getColumn() == col) {
                return box;
            }
        }
        return null;
    }

    // Verifica si una caja puede ser empujada en una dirección
    private boolean canPushBox(Box box, Direction direction) {
        Position nextPos = getNextPosition(box.getPosition(), direction);
        if (!board.isValidPosition(nextPos.getRow(), nextPos.getColumn())) return false;
        if (board.isWall(nextPos.getRow(), nextPos.getColumn())) return false;
        if (hasBox(nextPos.getRow(), nextPos.getColumn())) return false;
        return true;
    }

    public int getMoveCount() {
        return moveCount;
    }
    public int getPushCount() {
        return pushCount;
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

