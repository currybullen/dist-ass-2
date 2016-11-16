package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.grpc.Challenge;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class InitAuthCallHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(InitAuthCallHandler.class.getName());

    private String username;
    private String challenge;

    public InitAuthCallHandler(String username) {
        this.username = username;
    }

    public EncryptedMessage setUp() {
        return MessageBuilder.buildUsernameMessage(username);
    }

    public void handleResponse(EncryptedMessage encryptedMessage) {
        try {
            Challenge challenge = Challenge.parseFrom(decryptMessage(encryptedMessage));
            this.challenge = challenge.getValue();
            logger.info("Challenge received: '" + challenge.getValue() + "'.");
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public String getChallenge() {
        return challenge;
    }
}
