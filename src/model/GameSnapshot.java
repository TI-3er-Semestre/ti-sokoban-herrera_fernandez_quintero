package model;

import struture.CustomLinkedList;

public class GameSnapshot {

    private Position playerPosition;
    private CustomLinkedList<Position> boxPositions;

    public GameSnapshot(Position playerPosition, CustomLinkedList<Box> boxes) {
        // Copiar la posición del jugador
        this.playerPosition = new Position(playerPosition.getRow(), playerPosition.getColumn());

        // Copiar las posiciones de todas las cajas
        this.boxPositions = new CustomLinkedList<>();
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            boxPositions.add(new Position(box.getPosition().getRow(), box.getPosition().getColumn()));
        }
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public CustomLinkedList<Position> getBoxPositions() {
        return boxPositions;
    }
}