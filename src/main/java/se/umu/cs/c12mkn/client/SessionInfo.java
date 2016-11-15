package se.umu.cs.c12mkn.client;

import se.umu.cs.c12mkn.shared.security.DHKeyExchange;

import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by c12mkn on 11/14/16.
 */
public class SessionInfo {
    private static SessionInfo instance = new SessionInfo();

    private final DHParameterSpec dhParameterSpec;
    private final KeyPair dhKeys;
    private final PublicKey serverPublicSignKey;
    private String id;
    private SecretKey secretKey;

    private SessionInfo() {
        dhParameterSpec = DHKeyExchange.generateParameters();
        dhKeys = DHKeyExchange.generateKeyPair(dhParameterSpec.getP(), dhParameterSpec.getG());
        serverPublicSignKey = loadServerPublicSignKey("/home/c12/c12mkn/edu/5DV153/assignments/2/src/main/resources/certificate/pubkey");
    }

    public static SessionInfo getInstance() {
        return instance;
    }

    public PrivateKey getDHPrivateKey() {
        return dhKeys.getPrivate();
    }

    public PublicKey getDHPublicKey() {
        return dhKeys.getPublic();
    }

    public BigInteger getDHModulus() {
        return dhParameterSpec.getP();
    }

    public BigInteger getDHBase() {
        return dhParameterSpec.getG();
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
