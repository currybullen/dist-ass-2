package se.umu.cs.c12mkn.server.security;

import se.umu.cs.c12mkn.shared.security.DHKeyExchange;

import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by currybullen on 11/13/16.
 */
public class Secrets {
    private static final Secrets instance = new Secrets();

    private Map<String, SecretKey> secrets;

    private Secrets() {
        secrets = new HashMap<String, SecretKey>();
    }

    public static Secrets getInstance() {
        return instance;
    }

    public void addSecret(String session, SecretKey secretKey) {
        secrets.put(session, secretKey);
    }

    public SecretKey getSecret(String secret) {
        return secrets.get(secret);
    }

    public KeyPair generateKeyPair(byte[] modulus, byte[] base) {
        KeyPair keyPair = null;
        try {
            keyPair = DHKeyExchange.generateKeyPair(modulus, base);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return keyPair;
    }

    public SecretKey generateSecret(KeyPair keyPair, byte[] publicKey, String algorithm) {
        SecretKey secretKey = null;
        try {
            secretKey = DHKeyExchange.generateSecretKey(keyPair.getPrivate(), publicKey, algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return secretKey;
    }
}
