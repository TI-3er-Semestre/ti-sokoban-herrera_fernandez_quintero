package model;


public class Player {
    private Position position;
    private String name;


    public Player(Position position) {
        this.position = position;
        this.name = "Player";
    }

    public Player(Position position, String name) {
        this.position = position;
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void moveTo(int newRow, int newColumn) {
        this.position.setRow(newRow);
        this.position.setColumn(newColumn);
    }
}
