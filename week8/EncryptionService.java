// For base 64 encryption
import java.util.Base64;

interface EncryptionStrategy {
    String encrypt(String text);
}

class CaesarCipherEncryption implements  EncryptionStrategy {
    private int shift;

    public CaesarCipherEncryption(int shift) {
        this.shift = shift;
    }

    @Override 
    public String encrypt(String text) {
        StringBuilder result = new StringBuilder();
        // toCharArray converts a string to array characters
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                ch = (char) ((ch - base + shift) % 26 + base);
            }
            result.append(ch);
        }
    return result.toString();
    }
}

class Base64Encryption implements EncryptionStrategy {
    @Override
    public String encrypt(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }
}

class XOREncryption implements EncryptionStrategy {
    char key;

    public XOREncryption(char key) {
        this.key = key;
    }

    @Override
    public String encrypt(String text) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            result.append((char) (ch ^ key));
        }
        return result.toString();
    }
}

class ReverseStringEncryption implements EncryptionStrategy {
    @Override
    public String encrypt(String text) {
        StringBuilder reversed = new StringBuilder(text);
        return reversed.reverse().toString();        
    }
}

class DuplicateCharacterEncryption implements EncryptionStrategy {
    @Override
    public String encrypt(String text) {
        StringBuilder result = new StringBuilder();
        for (char chr : text.toCharArray()) {
            result.append(chr);
            result.append(chr);
        }
        return result.toString();
    }
}

class EncryptionService {
    private EncryptionStrategy EncryptionStrategy;

    public void setEncryptionStrategy(EncryptionStrategy strategy) {
        this.EncryptionStrategy = strategy;
    }

    public String encrypt(String text) {
        return EncryptionStrategy.encrypt(text);
    }
}