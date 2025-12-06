package storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HighScore implements Comparable<HighScore> {
    private String username;
    private int score;
    private String gameType;
    private LocalDateTime timestamp;

    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HighScore(String username, int score, String gameType) {
        this.username = username;
        this.score = score;
        this.gameType = gameType;
        this.timestamp = LocalDateTime.now();
    }

    public HighScore(String username, int score, String gameType, LocalDateTime timestamp) {
        this.username = username;
        this.score = score;
        this.gameType = gameType;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public String getGameType() {
        return gameType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String toFileFormat() {
        return username + "," + score + "," + gameType + "," + timestamp.format(FORMATTER);
    }

    public static HighScore fromFileFormat(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split(",");
        if (parts.length >= 4) {
            try {
                String username = parts[0].trim();
                int score = Integer.parseInt(parts[1].trim());
                String gameType = parts[2].trim();
                LocalDateTime timestamp = LocalDateTime.parse(parts[3].trim(), FORMATTER);
                return new HighScore(username, score, gameType, timestamp);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public int compareTo(HighScore other) {
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return username + " - " + score + " (" + gameType + ")";
    }

    public String toDisplayString(int rank) {
        return String.format("%d. %s - %d pts", rank, username, score);
    }

    public String getFormattedTimestamp() {
        return timestamp.format(FORMATTER);
    }
}

