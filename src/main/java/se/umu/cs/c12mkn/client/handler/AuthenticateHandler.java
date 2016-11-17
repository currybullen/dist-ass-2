package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Succeeded;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class AuthenticateHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(AuthenticateHandler.class.getName());

    private String username;
    private String challenge;
    private String answer;

    public AuthenticateHandler(String username, String challenge, String answer) {
        this.username = username;
        this.challenge = challenge;
        this.answer = answer;
    }

    public EncryptedMessage setUp() {
        logger.info("Sending authentication request");
        return encryptMessage(MessageBuilder.buildAuthResponseMessage(username, challenge, answer).toByteArray());
    }

    public boolean handleResponse(EncryptedMessage response) {
        try {
            Succeeded succeeded = Succeeded.parseFrom(decryptMessage(response));
            if (succeeded.getValue())
                logger.info("Successfully authenticated.");
            return succeeded.getValue();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return false;
    }
}
