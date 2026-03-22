package model;

public class Box {
    private Position position;
    private boolean onGoal;

    public Box(Position position) {
        this.position = position;
        this.onGoal = false;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


    public boolean isOnGoal() {
        return onGoal;
    }

    public void setOnGoal(boolean onGoal) {
        this.onGoal = onGoal;
    }

    public void moveTo(int newRow, int newColumn) {
        this.position.setRow(newRow);
        this.position.setColumn(newColumn);
    }
}
