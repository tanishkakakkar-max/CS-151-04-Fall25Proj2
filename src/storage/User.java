package storage;

import utils.EncryptionUtils;

public class User {
    private String username;
    private String password;

    public User() {
        this.username = "";
        this.password = "";
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public User(String username, String encryptedPassword, boolean isEncrypted) {
        this.username = username;
        this.password = encryptedPassword;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean validateCredentials(String username, String plainPassword) {
        if (!this.username.equals(username)) {
            return false;
        }
        return EncryptionUtils.validatePassword(plainPassword, this.password);
    }

    public String toFileFormat() {
        return username + "," + password;
    }

    public static User fromFileFormat(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split(",");
        if (parts.length >= 2) {
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

    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
               password != null && !password.isEmpty();
    }
}

