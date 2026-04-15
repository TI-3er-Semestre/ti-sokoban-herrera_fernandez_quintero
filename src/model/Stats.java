package model;

public class Stats implements Comparable<Stats> {

    private String playerId;
    private String playerName;
    private int level;
    private int movements;
    private int pushes;
    private int time;
    private String date;
    private boolean completed;
    private double score;

    public Stats(String playerId, String playerName, int level, int movements,
                 int pushes, int time, boolean completed, String date) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.level = level;
        this.movements = movements;
        this.pushes = pushes;
        this.time = time;
        this.completed = completed;
        this.date = date;
        this.score = calculateScore();
    }

    // Calcula el puntaje: menor movimientos, empujes y tiempo = mayor puntaje
    public double calculateScore() {
        if (!completed) return 0.0;
        if (movements == 0 || time == 0) return 0.0;
        return (1000.0 / movements) + (500.0 / (pushes + 1)) + (2000.0 / time);
    }

    @Override
    public int compareTo(Stats other) {
        return Double.compare(other.score, this.score);
    }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getMovements() { return movements; }
    public void setMovements(int movements) {
        this.movements = movements;
        this.score = calculateScore();
    }

    public int getPushes() { return pushes; }
    public void setPushes(int pushes) {
        this.pushes = pushes;
        this.score = calculateScore();
    }

    public int getTime() { return time; }
    public void setTime(int time) {
        this.time = time;
        this.score = calculateScore();
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.score = calculateScore();
    }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    @Override
    public String toString() {
        return "Stats{player='" + playerName + "', level=" + level +
                ", movements=" + movements + ", pushes=" + pushes +
                ", time=" + time + ", score=" + score + "}";
    }
}
