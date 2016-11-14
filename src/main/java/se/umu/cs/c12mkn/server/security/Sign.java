package se.umu.cs.c12mkn.server.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by currybullen on 11/13/16.
 */
public class Sign {
    private static final Sign instance = new Sign();
    private PrivateKey privateKey;
    private boolean isInitialized;

    private Sign() {
        isInitialized = false;
    }

    public static Sign getInstance() {
        return instance;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setPrivateKey(String pathToPrivateKey) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        if (!isInitialized) {
            byte[] keyBytes = Files.readAllBytes(new File(pathToPrivateKey).toPath());
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            isInitialized = true;
        }
    }

    public byte[] sign(byte[] data) {
        try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            return null;
        }
    }
}
