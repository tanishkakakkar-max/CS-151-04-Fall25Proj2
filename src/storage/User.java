package storage;

/**
 * User class representing a user account in the game system.
 * Stores username and password for authentication.
 */
public class User {
    private String username;
    private String password;

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
     * @param password the user's password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
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
     * @param password password to check
     * @return true if credentials match
     */
    public boolean validateCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Converts user to file format string.
     * @return formatted string for file storage
     */
    public String toFileFormat() {
        return username + "," + password;
    }

    /**
     * Creates User from file format string.
     * @param line the line from file
     * @return User object or null if invalid
     */
    public static User fromFileFormat(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split(",");
        if (parts.length >= 2) {
            return new User(parts[0].trim(), parts[1].trim());
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

