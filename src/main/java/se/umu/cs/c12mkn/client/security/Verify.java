package se.umu.cs.c12mkn.client.security;

import java.security.*;

/**
 * Created by c12mkn on 11/14/16.
 */
public class Verify {
    public static boolean verify(byte[] data, byte[] dataSignature, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(dataSignature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return false;
    }
}
