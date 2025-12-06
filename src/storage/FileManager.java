package storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import utils.EncryptionUtils;

public class FileManager {
    
    private static final String ACCOUNTS_FILE = "user_accounts.txt";
    private static final String HIGH_SCORES_FILE = "high_scores.txt";
    
    private List<User> users;
    private List<HighScore> highScores;

    public FileManager() {
        this.users = new ArrayList<>();
        this.highScores = new ArrayList<>();
        initializeFiles();
        loadAllData();
    }

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

    public void loadAllData() {
        loadUsers();
        loadHighScores();
    }

    public void loadUsers() {
        users.clear();
        boolean needsMigration = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromFileFormat(line);
                if (user != null) {
                    String password = user.getPassword();
                    if (password != null && !password.isEmpty() && !EncryptionUtils.isEncrypted(password)) {
                        String encryptedPassword = EncryptionUtils.encrypt(password);
                        user.setPassword(encryptedPassword);
                        needsMigration = true;
                    }
                    users.add(user);
                }
            }
            if (needsMigration) {
                saveUsers();
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

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

    public boolean addUser(String username, String password) {
        if (userExists(username)) {
            return false;
        }
        String encryptedPassword = EncryptionUtils.encrypt(password);
        User newUser = new User(username, encryptedPassword, true);
        users.add(newUser);
        saveUsers();
        return true;
    }

    public boolean userExists(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean validateLogin(String username, String password) {
        for (User user : users) {
            if (user.validateCredentials(username, password)) {
                return true;
            }
        }
        return false;
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void addHighScore(String username, int score, String gameType) {
        HighScore newScore = new HighScore(username, score, gameType);
        highScores.add(newScore);
        sortHighScores();
        saveHighScores();
    }

    private void sortHighScores() {
        highScores.sort(null);
    }

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

    public List<HighScore> getTop5Scores(String gameType) {
        return getTopScores(gameType, 5);
    }

    public List<HighScore> getAllHighScores() {
        return new ArrayList<>(highScores);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public int getUserCount() {
        return users.size();
    }

    public int getHighScoreCount() {
        return highScores.size();
    }
}

