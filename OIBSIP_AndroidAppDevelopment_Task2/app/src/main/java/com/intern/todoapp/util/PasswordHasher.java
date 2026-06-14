package com.intern.todoapp.util;

import android.util.Base64;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Password hashing with PBKDF2-HMAC-SHA256.
 *  - 16-byte per-user random salt
 *  - 120 000 iterations
 *  - 32-byte derived key
 * Stored format (single string): "iter:saltBase64:hashBase64"
 *
 * verify() uses constant-time comparison.
 */
public final class PasswordHasher {
    private static final String ALGO = "PBKDF2WithHmacSHA256";
    private static final int ITER = 120_000;
    private static final int KEY_LEN = 256;          // bits
    private static final int SALT_LEN = 16;          // bytes

    private PasswordHasher() {}

    public static String hash(String password) {
        byte[] salt = new byte[SALT_LEN];
        new SecureRandom().nextBytes(salt);
        byte[] hash = derive(password.toCharArray(), salt, ITER);
        return ITER + ":" + b64(salt) + ":" + b64(hash);
    }

    public static boolean verify(String password, String stored) {
        try {
            String[] parts = stored.split(":");
            int iter = Integer.parseInt(parts[0]);
            byte[] salt = unb64(parts[1]);
            byte[] expected = unb64(parts[2]);
            byte[] actual = derive(password.toCharArray(), salt, iter);
            return constantTimeEquals(expected, actual);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] derive(char[] password, byte[] salt, int iter) {
        try {
            KeySpec spec = new PBEKeySpec(password, salt, iter, KEY_LEN);
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGO);
            return f.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("PBKDF2 failure", e);
        }
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) result |= a[i] ^ b[i];
        return result == 0;
    }

    private static String b64(byte[] in) { return Base64.encodeToString(in, Base64.NO_WRAP); }
    private static byte[] unb64(String s) { return Base64.decode(s, Base64.NO_WRAP); }
}
