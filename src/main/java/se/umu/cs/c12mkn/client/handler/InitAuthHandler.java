package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.grpc.Challenge;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class InitAuthHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(InitAuthHandler.class.getName());

    private final String username;
    private String challenge;

    public InitAuthHandler(String username) {
        this.username = username;
    }

    public EncryptedMessage setUp() {
        return encryptMessage(MessageBuilder.buildUsernameMessage(username).toByteArray());
    }

    public boolean handleResponse(EncryptedMessage encryptedMessage) {
        try {
            Challenge challenge = Challenge.parseFrom(decryptMessage(encryptedMessage));
            this.challenge = challenge.getValue();
            logger.info("Challenge received: '" + challenge.getValue() + "'.");
            return true;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getChallenge() {
        return challenge;
    }
}
