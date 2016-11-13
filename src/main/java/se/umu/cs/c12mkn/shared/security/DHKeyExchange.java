package se.umu.cs.c12mkn.shared.security;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import java.security.*;

/**
 * Created by currybullen on 11/13/16.
 */
public class DHKeyExchange {
    public static KeyPair generateKeyPair(byte[] modulus, byte[] base) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return generateKeyPair(new BigInteger(modulus), new BigInteger(base));
    }

    public static KeyPair generateKeyPair(BigInteger modulus, BigInteger base) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        DHParameterSpec dhParameterSpec = new DHParameterSpec(modulus, base);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(dhParameterSpec);
        return keyPairGenerator.generateKeyPair();
    }

    public static SecretKey generateSecretKey(Key privateKey, byte[] publicKey, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        //TODO: Turn byte array into actual key here.
        return generateSecretKey(privateKey, (Key) null, algorithm);
    }

    public static SecretKey generateSecretKey(Key privateKey, Key publicKey, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init((DHPrivateKey) privateKey);
        keyAgreement.doPhase((DHPublicKey) publicKey, true);
        return keyAgreement.generateSecret(algorithm);
    }
}
