package model;

public class Player {

    // User registration data
    private String name;
    private String email;
    private String username;
    private String avatar;
    private ExperienceLevel experienceLevel;

    // Game statistics
    private int totalMovements;
    private int totalPushes;
    private int totalTime;

    // Position on board
    private Position position;

    public Player(String name, String email, String username, String avatar, ExperienceLevel experienceLevel) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.avatar = avatar;
        this.experienceLevel = experienceLevel;
        this.totalMovements = 0;
        this.totalPushes = 0;
        this.totalTime = 0;
    }

    // Getters and Setters - user data
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public ExperienceLevel getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(ExperienceLevel experienceLevel) { this.experienceLevel = experienceLevel; }

    // Getters and Setters - statistics
    public int getTotalMovements() { return totalMovements; }
    public void setTotalMovements(int totalMovements) { this.totalMovements = totalMovements; }

    public int getTotalPushes() { return totalPushes; }
    public void setTotalPushes(int totalPushes) { this.totalPushes = totalPushes; }

    public int getTotalTime() { return totalTime; }
    public void setTotalTime(int totalTime) { this.totalTime = totalTime; }

    // Getters and Setters - position
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public void moveTo(int newRow, int newColumn) {
        this.position.setRow(newRow);
        this.position.setColumn(newColumn);
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "', username='" + username + "', level=" + experienceLevel + "}";
    }
}
