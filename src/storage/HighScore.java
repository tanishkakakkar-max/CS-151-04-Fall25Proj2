package storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * HighScore class representing a high score entry.
 * Stores username, score, game type, and timestamp.
 */
public class HighScore implements Comparable<HighScore> {
    private String username;
    private int score;
    private String gameType; // "blackjack" or "snake"
    private LocalDateTime timestamp;

    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor for HighScore.
     * @param username the player's username
     * @param score the score achieved
     * @param gameType the type of game
     */
    public HighScore(String username, int score, String gameType) {
        this.username = username;
        this.score = score;
        this.gameType = gameType;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor with timestamp for loading from file.
     */
    public HighScore(String username, int score, String gameType, LocalDateTime timestamp) {
        this.username = username;
        this.score = score;
        this.gameType = gameType;
        this.timestamp = timestamp;
    }

    // Getters
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

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    /**
     * Converts high score to file format string.
     * @return formatted string for file storage
     */
    public String toFileFormat() {
        return username + "," + score + "," + gameType + "," + timestamp.format(FORMATTER);
    }

    /**
     * Creates HighScore from file format string.
     * @param line the line from file
     * @return HighScore object or null if invalid
     */
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

    /**
     * Compare by score (descending order for sorting).
     */
    @Override
    public int compareTo(HighScore other) {
        return Integer.compare(other.score, this.score); // Descending
    }

    @Override
    public String toString() {
        return username + " - " + score + " (" + gameType + ")";
    }

    /**
     * Gets formatted display string for high score board.
     * @param rank the ranking position (1-based)
     * @return formatted string for display
     */
    public String toDisplayString(int rank) {
        return String.format("%d. %s - %d pts", rank, username, score);
    }

    /**
     * Gets the formatted timestamp string.
     * @return formatted date/time string
     */
    public String getFormattedTimestamp() {
        return timestamp.format(FORMATTER);
    }
}

