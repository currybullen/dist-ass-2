package se.umu.cs.c12mkn.client.handler;

import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.message.Message;

/**
 * Created by currybullen on 11/16/16.
 */
public class PostMessageHandler extends CallHandler {
    private Message message;

    public PostMessageHandler(Message message) {
        this.message = message;
    }

    public EncryptedMessage setUp() {
        return encryptMessage(MessageBuilder.buildMessage(message).toByteArray());
    }
}
