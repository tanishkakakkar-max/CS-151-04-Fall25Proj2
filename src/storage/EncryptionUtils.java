package storage;

import java.util.Base64;

public class EncryptionUtils {
    
    private static final String SECRET_KEY = "CS151GameProject";

    public static String encrypt(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }
        
        byte[] passwordBytes = password.getBytes();
        byte[] keyBytes = SECRET_KEY.getBytes();
        byte[] encrypted = new byte[passwordBytes.length];
        
        for (int i = 0; i < passwordBytes.length; i++) {
            encrypted[i] = (byte) (passwordBytes[i] ^ keyBytes[i % keyBytes.length]);
        }
        
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedPassword) {
        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
            return "";
        }
        
        try {
            byte[] encrypted = Base64.getDecoder().decode(encryptedPassword);
            byte[] keyBytes = SECRET_KEY.getBytes();
            byte[] decrypted = new byte[encrypted.length];
            
            for (int i = 0; i < encrypted.length; i++) {
                decrypted[i] = (byte) (encrypted[i] ^ keyBytes[i % keyBytes.length]);
            }
            
            return new String(decrypted);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean validatePassword(String plainPassword, String encryptedPassword) {
        String encrypted = encrypt(plainPassword);
        return encrypted.equals(encryptedPassword);
    }

    public static boolean isEncrypted(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        try {
            Base64.getDecoder().decode(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

