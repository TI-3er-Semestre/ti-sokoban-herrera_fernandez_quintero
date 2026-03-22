package model;

import util.CustomLinkedList;

public class Board {
    private char[][] grid;
    private int width;
    private int height;
    private CustomLinkedList<Position> goals;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        this.goals = new CustomLinkedList<>();
        initializeBoard();
    }

    private void initializeBoard() {

        throw new UnsupportedOperationException("Not implemented yet");
    }

    public char getCell(int row, int col) {

        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setCell(int row, int col, char value) {

        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean isWall(int row, int col) {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public boolean isValidPosition(int row, int col) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean isGoal(int row, int col) {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public void addGoal(Position position) {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public CustomLinkedList<Position> getGoals() {
        return goals;
    }


    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }

    public char[][] getGrid() {
        return grid;
    }
}
