package com.example.streak.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class Encrypt {
    public static String encrypt(String rawPassword) {
        byte[] digest = null;
        try {
            digest = sha256(rawPassword);
        } catch (Exception e) {
            log.error("", e);
        }

        return bytesToHex(digest);
    }

    private static byte[] sha256(String rawPassword) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        return digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b: bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
