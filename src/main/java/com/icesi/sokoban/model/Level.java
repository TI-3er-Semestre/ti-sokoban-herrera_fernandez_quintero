package com.icesi.sokoban.model;

public class Level {
    private int levelId;
    private String name;
    private Board board;
    private Position playerStartPosition;
    private String difficulty;


    public Level(int levelId, String name) {
        this.levelId = levelId;
        this.name = name;
    }


    public void loadFromJson(String jsonString) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getLevelId() {
        return levelId;
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Position getPlayerStartPosition() {
        return playerStartPosition;
    }

    public void setPlayerStartPosition(Position position) {
        this.playerStartPosition = position;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
