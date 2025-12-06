package utils;

import java.util.Base64;

/**
 * EncryptionUtils provides simple encryption/decryption for passwords and save states.
 * Uses Base64 encoding with a simple XOR cipher for basic security.
 */
public class EncryptionUtils {
    
    // Simple key for XOR encryption
    private static final String SECRET_KEY = "CS151GameProject";

    /**
     * Encrypts a string using XOR cipher and Base64 encoding.
     * @param plainText the plain text to encrypt
     * @return encrypted string
     */
    public static String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return "";
        }
        
        byte[] textBytes = plainText.getBytes();
        byte[] keyBytes = SECRET_KEY.getBytes();
        byte[] encrypted = new byte[textBytes.length];
        
        for (int i = 0; i < textBytes.length; i++) {
            encrypted[i] = (byte) (textBytes[i] ^ keyBytes[i % keyBytes.length]);
        }
        
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Decrypts an encrypted string.
     * @param encryptedText the encrypted string
     * @return decrypted plain text
     */
    public static String decrypt(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return "";
        }
        
        try {
            byte[] encrypted = Base64.getDecoder().decode(encryptedText);
            byte[] keyBytes = SECRET_KEY.getBytes();
            byte[] decrypted = new byte[encrypted.length];
            
            for (int i = 0; i < encrypted.length; i++) {
                decrypted[i] = (byte) (encrypted[i] ^ keyBytes[i % keyBytes.length]);
            }
            
            return new String(decrypted);
        } catch (Exception e) {
            // If decryption fails, return empty string
            return "";
        }
    }

    /**
     * Validates if a plain password matches an encrypted password.
     * @param plainPassword the plain text password to check
     * @param encryptedPassword the stored encrypted password
     * @return true if passwords match
     */
    public static boolean validatePassword(String plainPassword, String encryptedPassword) {
        String encrypted = encrypt(plainPassword);
        return encrypted.equals(encryptedPassword);
    }

    /**
     * Checks if a string appears to be encrypted (Base64 format).
     * @param text the text to check
     * @return true if text appears to be encrypted
     */
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

