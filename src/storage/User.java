package storage;

import utils.EncryptionUtils;

/**
 * User class representing a user account in the game system.
 * Stores username and password for authentication.
 * Passwords are encrypted when stored.
 */
public class User {
    private String username;
    private String password; // This will store encrypted password

    /**
     * Default constructor for User.
     */
    public User() {
        this.username = "";
        this.password = "";
    }

    /**
     * Constructor with username and password.
     * @param username the user's username
     * @param password the user's password (will be encrypted)
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password; // Store as-is (will be encrypted by FileManager)
    }
    
    /**
     * Constructor with username and encrypted password (for loading from file).
     * @param username the user's username
     * @param encryptedPassword the encrypted password
     * @param isEncrypted flag to indicate password is already encrypted
     */
    public User(String username, String encryptedPassword, boolean isEncrypted) {
        this.username = username;
        this.password = encryptedPassword; // Already encrypted
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Validates if provided credentials match this user.
     * @param username username to check
     * @param plainPassword plain text password to check
     * @return true if credentials match
     */
    public boolean validateCredentials(String username, String plainPassword) {
        if (!this.username.equals(username)) {
            return false;
        }
        // Compare encrypted versions
        return EncryptionUtils.validatePassword(plainPassword, this.password);
    }

    /**
     * Converts user to file format string.
     * Password is already encrypted when this is called.
     * @return formatted string for file storage
     */
    public String toFileFormat() {
        return username + "," + password; // password is already encrypted
    }

    /**
     * Creates User from file format string.
     * Password is already encrypted in the file.
     * @param line the line from file
     * @return User object or null if invalid
     */
    public static User fromFileFormat(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split(",");
        if (parts.length >= 2) {
            // Password is already encrypted in file
            return new User(parts[0].trim(), parts[1].trim(), true);
        }
        return null;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    /**
     * Checks if username is valid (not null or empty).
     * @return true if username is valid
     */
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
               password != null && !password.isEmpty();
    }
}

