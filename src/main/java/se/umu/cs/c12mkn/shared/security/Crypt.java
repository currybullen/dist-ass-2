package se.umu.cs.c12mkn.shared.security;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by c12mkn on 11/15/16.
 */
public class Crypt {
    public static byte[] decrypt(byte[] data, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(),
                    secretKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    public static byte[] encrypt(byte[] data, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(),
                    secretKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }
}
