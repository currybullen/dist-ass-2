package se.umu.cs.c12mkn.client.handler;

import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;

/**
 * Created by currybullen on 11/16/16.
 */
public class AuthenticateHandler extends CallHandler {
    private String username;
    private String challenge;
    private String answer;

    public AuthenticateHandler(String username, String challenge, String answer) {
        this.username = username;
        this.challenge = challenge;
        this.answer = answer;
    }

    public EncryptedMessage setUp() {
        return encryptMessage(MessageBuilder.buildAuthResponseMessage(username, challenge, answer).toByteArray());
    }
}
