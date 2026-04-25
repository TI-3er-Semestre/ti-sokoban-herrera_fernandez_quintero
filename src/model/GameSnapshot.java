package model;

import structure.CustomLinkedList;

public class GameSnapshot {

    private Position playerPosition;
    private CustomLinkedList<Position> boxPositions;
    private int moveCount;
    private int pushCount;

    public GameSnapshot(Position playerPosition, CustomLinkedList<Box> boxes, int moveCount, int pushCount) {
        // Copiar la posición del jugador
        this.playerPosition = new Position(playerPosition.getRow(), playerPosition.getColumn());

        // Copiar las posiciones de todas las cajas
        this.boxPositions = new CustomLinkedList<>();
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            boxPositions.add(new Position(box.getPosition().getRow(), box.getPosition().getColumn()));
        }
        this.moveCount = moveCount;
        this.pushCount = pushCount;
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public CustomLinkedList<Position> getBoxPositions() {
        return boxPositions;
    }

    public int getMoveCount() { return moveCount; }

    public int getPushCount() { return pushCount; }
}