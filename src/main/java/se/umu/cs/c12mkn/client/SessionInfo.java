package se.umu.cs.c12mkn.client;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by c12mkn on 11/14/16.
 */
public class SessionInfo {
    private static SessionInfo instance = new SessionInfo();

    private PublicKey serverPublicKey;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String id;
    private SecretKey secretKey;
    private String algorithm;

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public static SessionInfo getInstance() {
        return instance;
    }

    public void setServerPublicKey(String path) {
        serverPublicKey = loadServerPublicSignKey(path);
    }

    public PublicKey getServerPublicKey() {
        return serverPublicKey;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    private PublicKey loadServerPublicSignKey(String path) {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(path).toPath());
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
