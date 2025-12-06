import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import utils.EncryptionUtils;

public class EncryptionUtilsTest {

    @Test
    public void testEncryptDecrypt() {
        String plainText = "testPassword123";
        String encrypted = EncryptionUtils.encrypt(plainText);
        assertNotNull(encrypted);
        assertNotEquals(plainText, encrypted);
        
        String decrypted = EncryptionUtils.decrypt(encrypted);
        assertEquals(plainText, decrypted);
    }

    @Test
    public void testEncryptEmptyString() {
        String encrypted = EncryptionUtils.encrypt("");
        assertEquals("", encrypted);
    }

    @Test
    public void testEncryptNull() {
        String encrypted = EncryptionUtils.encrypt(null);
        assertEquals("", encrypted);
    }

    @Test
    public void testDecryptEmptyString() {
        String decrypted = EncryptionUtils.decrypt("");
        assertEquals("", decrypted);
    }

    @Test
    public void testDecryptNull() {
        String decrypted = EncryptionUtils.decrypt(null);
        assertEquals("", decrypted);
    }

    @Test
    public void testValidatePassword() {
        String password = "myPassword";
        String encrypted = EncryptionUtils.encrypt(password);
        
        assertTrue(EncryptionUtils.validatePassword(password, encrypted));
        assertFalse(EncryptionUtils.validatePassword("wrongPassword", encrypted));
    }

    @Test
    public void testIsEncrypted() {
        String nonBase64 = "Hello World!@#";
        assertFalse(EncryptionUtils.isEncrypted(nonBase64));
        
        String encrypted = EncryptionUtils.encrypt("test");
        assertTrue(EncryptionUtils.isEncrypted(encrypted));
    }

    @Test
    public void testIsEncryptedNull() {
        assertFalse(EncryptionUtils.isEncrypted(null));
    }

    @Test
    public void testIsEncryptedEmpty() {
        assertFalse(EncryptionUtils.isEncrypted(""));
    }

    @Test
    public void testEncryptDecryptSpecialCharacters() {
        String special = "!@#$%^&*()";
        String encrypted = EncryptionUtils.encrypt(special);
        String decrypted = EncryptionUtils.decrypt(encrypted);
        assertEquals(special, decrypted);
    }

    @Test
    public void testEncryptDecryptLongString() {
        String longText = "a".repeat(1000);
        String encrypted = EncryptionUtils.encrypt(longText);
        String decrypted = EncryptionUtils.decrypt(encrypted);
        assertEquals(longText, decrypted);
    }
}

