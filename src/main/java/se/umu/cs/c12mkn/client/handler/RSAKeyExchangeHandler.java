package se.umu.cs.c12mkn.client.handler;

import se.umu.cs.c12mkn.client.SessionInfo;
import se.umu.cs.c12mkn.client.builder.MessageBuilder;
import se.umu.cs.c12mkn.grpc.RSARequest;
import se.umu.cs.c12mkn.grpc.Session;

import java.security.*;
import java.util.logging.Logger;

/**
 * Created by c12mkn on 11/17/16.
 */
public class RSAKeyExchangeHandler {
    private static final Logger logger = Logger.getLogger(RSAKeyExchangeHandler.class.getName());

    public RSARequest setUp() {
        KeyPair keyPair = generateKeyPair();
        SessionInfo.getInstance().setAlgorithm("RSA");
        SessionInfo.getInstance().setPrivateKey(keyPair.getPrivate());
        SessionInfo.getInstance().setPublicKey(keyPair.getPublic());
        return MessageBuilder.buildRSARequestMessage(keyPair.getPublic());
    }

    public boolean handleResponse(Session session) {
        SessionInfo.getInstance().setID(session.getValue());
        return true;
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
