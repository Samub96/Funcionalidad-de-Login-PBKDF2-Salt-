package com.security.login;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordUtils {
    private static final int SALT_LEN = 16;
    private static final int ITERATIONS = 100_000;
    private static final int KEY_LENGTH = 256;

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LEN];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] hashPassword(String password, byte[] salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }

    public static boolean verifyPassword(String password, byte[] salt, byte[] expectedHash, int iterations) {
        byte[] candidate = hashPassword(password, salt, iterations);
        return Arrays.equals(candidate, expectedHash);
    }

    public static int getIterations() {
        return ITERATIONS;
    }
}
