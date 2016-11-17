package se.umu.cs.c12mkn.server.handler;

import se.umu.cs.c12mkn.grpc.RSARequest;
import se.umu.cs.c12mkn.grpc.Session;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.security.Sessions;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by c12mkn on 11/17/16.
 */
public class RSAKeyExchangeHandler extends CallHandler {
    public Session handle(RSARequest rsaRequest) {
        PublicKey publicKey = bytesToPublicKey(rsaRequest.getPublicKey().toByteArray());
        String session = Sessions.getInstance().createSession("RSA", publicKey);
        return MessageBuilder.buildSessionMessage(session);
    }

    private PublicKey bytesToPublicKey(byte[] publicKey) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }
}
