package model;

import struture.CustomLinkedList;

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

    // Llena el grid con espacios vacíos
    private void initializeBoard() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = ' ';
            }
        }
    }

    // Retorna el valor de una celda
    public char getCell(int row, int col) {
        return grid[row][col];
    }

    // Asigna un valor a una celda
    public void setCell(int row, int col, char value) {
        grid[row][col] = value;
    }

    // Verifica si una celda es muro
    public boolean isWall(int row, int col) {
        return grid[row][col] == '#';
    }

    // Verifica si la posición existe dentro del tablero
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    // Verifica si una posición es objetivo
    public boolean isGoal(int row, int col) {
        for (int i = 0; i < goals.size(); i++) {
            Position goal = goals.get(i);
            if (goal.getRow() == row && goal.getColumn() == col) {
                return true;
            }
        }
        return false;
    }

    // Agrega una posición objetivo a la lista
    public void addGoal(Position position) {
        goals.add(position);
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
