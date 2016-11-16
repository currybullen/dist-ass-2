package se.umu.cs.c12mkn.shared.security;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by currybullen on 11/13/16.
 */
public class DHKeyExchange {
    public static KeyPair generateKeyPair(byte[] modulus, byte[] base) {
        return generateKeyPair(new BigInteger(modulus), new BigInteger(base));
    }

    public static KeyPair generateKeyPair(BigInteger modulus, BigInteger base) {
        try {
            DHParameterSpec dhParameterSpec = new DHParameterSpec(modulus, base);
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(dhParameterSpec);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SecretKey generateSecretKey(Key privateKey, byte[] publicKey)  {
        return generateSecretKey(privateKey, bytesToPublicKey(publicKey));
    }

    public static SecretKey generateSecretKey(Key privateKey, Key publicKey) {
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(publicKey, true);
            return keyAgreement.generateSecret("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static DHParameterSpec generateParameters() {
        try {
            AlgorithmParameterGenerator generator = AlgorithmParameterGenerator.getInstance("DH");
            generator.init(1024);
            AlgorithmParameters parameters = generator.generateParameters();
            DHParameterSpec parameterSpec = parameters.getParameterSpec(DHParameterSpec.class);
            return parameterSpec;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static PublicKey bytesToPublicKey(byte[] publicKey) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }
}
