package storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import utils.EncryptionUtils;

/**
 * FileManager handles all file I/O operations for user accounts and high scores.
 * Manages reading and writing to user_accounts.txt and high_scores.txt.
 */
public class FileManager {
    
    private static final String ACCOUNTS_FILE = "user_accounts.txt";
    private static final String HIGH_SCORES_FILE = "high_scores.txt";
    
    private List<User> users;
    private List<HighScore> highScores;

    /**
     * Constructor initializes the FileManager and loads existing data.
     */
    public FileManager() {
        this.users = new ArrayList<>();
        this.highScores = new ArrayList<>();
        initializeFiles();
        loadAllData();
    }

    /**
     * Creates data files if they don't exist.
     */
    private void initializeFiles() {
        try {
            File accountsFile = new File(ACCOUNTS_FILE);
            if (!accountsFile.exists()) {
                accountsFile.createNewFile();
            }
            
            File scoresFile = new File(HIGH_SCORES_FILE);
            if (!scoresFile.exists()) {
                scoresFile.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error initializing files: " + e.getMessage());
        }
    }

    /**
     * Loads all data from files.
     */
    public void loadAllData() {
        loadUsers();
        loadHighScores();
    }

    /**
     * Loads users from user_accounts.txt.
     * Handles both encrypted and plain text passwords for backward compatibility.
     */
    public void loadUsers() {
        users.clear();
        boolean needsMigration = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromFileFormat(line);
                if (user != null) {
                    // Check if password is plain text (not encrypted) and encrypt it
                    String password = user.getPassword();
                    if (password != null && !password.isEmpty() && !EncryptionUtils.isEncrypted(password)) {
                        // Migrate plain text password to encrypted
                        String encryptedPassword = EncryptionUtils.encrypt(password);
                        user.setPassword(encryptedPassword);
                        needsMigration = true;
                    }
                    users.add(user);
                }
            }
            // Save users back to file if any were migrated
            if (needsMigration) {
                saveUsers();
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    /**
     * Loads high scores from high_scores.txt.
     */
    public void loadHighScores() {
        highScores.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                HighScore score = HighScore.fromFileFormat(line);
                if (score != null) {
                    highScores.add(score);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
        }
        sortHighScores();
    }

    /**
     * Saves all users to file.
     */
    public void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (User user : users) {
                writer.write(user.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Saves all high scores to file.
     */
    public void saveHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORES_FILE))) {
            for (HighScore score : highScores) {
                writer.write(score.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    // User management methods

    /**
     * Adds a new user account.
     * Password will be encrypted before storage.
     * @param username the username
     * @param password the plain text password
     * @return true if account created successfully
     */
    public boolean addUser(String username, String password) {
        if (userExists(username)) {
            return false;
        }
        // Encrypt password before storing
        String encryptedPassword = EncryptionUtils.encrypt(password);
        User newUser = new User(username, encryptedPassword, true);
        users.add(newUser);
        saveUsers();
        return true;
    }

    /**
     * Checks if a username already exists.
     * @param username the username to check
     * @return true if username exists
     */
    public boolean userExists(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates user login credentials.
     * @param username the username
     * @param password the password
     * @return true if credentials are valid
     */
    public boolean validateLogin(String username, String password) {
        for (User user : users) {
            if (user.validateCredentials(username, password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a user by username.
     * @param username the username to find
     * @return User object or null if not found
     */
    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // High score management methods

    /**
     * Adds a new high score.
     * @param username the player's username
     * @param score the score achieved
     * @param gameType the type of game
     */
    public void addHighScore(String username, int score, String gameType) {
        HighScore newScore = new HighScore(username, score, gameType);
        highScores.add(newScore);
        sortHighScores();
        saveHighScores();
    }

    /**
     * Sorts high scores in descending order.
     */
    private void sortHighScores() {
        highScores.sort(null); // Uses Comparable implementation
    }

    /**
     * Gets top N high scores for a specific game.
     * @param gameType the type of game
     * @param limit maximum number of scores to return
     * @return list of top high scores
     */
    public List<HighScore> getTopScores(String gameType, int limit) {
        List<HighScore> filtered = new ArrayList<>();
        for (HighScore score : highScores) {
            if (score.getGameType().equalsIgnoreCase(gameType)) {
                filtered.add(score);
                if (filtered.size() >= limit) {
                    break;
                }
            }
        }
        return filtered;
    }

    /**
     * Gets top 5 high scores for a game.
     * @param gameType the type of game
     * @return list of top 5 high scores
     */
    public List<HighScore> getTop5Scores(String gameType) {
        return getTopScores(gameType, 5);
    }

    /**
     * Gets all high scores.
     * @return list of all high scores
     */
    public List<HighScore> getAllHighScores() {
        return new ArrayList<>(highScores);
    }

    /**
     * Gets all users.
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Gets the total number of registered users.
     * @return user count
     */
    public int getUserCount() {
        return users.size();
    }

    /**
     * Gets the total number of high scores.
     * @return high score count
     */
    public int getHighScoreCount() {
        return highScores.size();
    }
}

