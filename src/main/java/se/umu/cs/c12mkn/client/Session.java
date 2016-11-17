package se.umu.cs.c12mkn.client;

import se.umu.cs.c12mkn.shared.security.DHKeyExchange;

import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by c12mkn on 11/14/16.
 */
public class Session {
    private static Session instance = new Session();

    private final DHParameterSpec dhParameterSpec;
    private final KeyPair dhKeys;
    private PublicKey serverPublicSignKey;
    private String id;
    private SecretKey secretKey;
    private String algorithm;

    private Session() {
        dhParameterSpec = DHKeyExchange.generateParameters();
        dhKeys = DHKeyExchange.generateKeyPair(dhParameterSpec.getP(), dhParameterSpec.getG());
    }

    public static Session getInstance() {
        return instance;
    }

    public void setServerPublicSignKey(String path) {
        serverPublicSignKey = loadServerPublicSignKey(path);
    }

    public PublicKey getServerPublicSignKey() {
        return serverPublicSignKey;
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
