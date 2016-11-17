package se.umu.cs.c12mkn.server.security;

import java.security.*;

/**
 * Created by currybullen on 11/13/16.
 */
public class Sign {
    public static byte[] sign(byte[] data, PrivateKey privateKey) {
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
