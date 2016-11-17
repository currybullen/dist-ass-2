package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Succeeded;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class UnsubscribeHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(UnsubscribeHandler.class.getName());
    private String username;
    private String topic;
    private boolean succeeded;

    public UnsubscribeHandler(String username, String topic) {
        this.username = username;
        this.topic = topic;
        succeeded = false;
    }

    public EncryptedMessage setUp() {
        logger.info("Sending unsubscribe request for user " + username + " and topic " + topic + ".");
        return encryptMessage(MessageBuilder.buildSubscriberInfoMessage(username, topic).toByteArray());
    }

    public void handleResponse(EncryptedMessage response) {
        try {
            Succeeded succeeded = Succeeded.parseFrom(decryptMessage(response));
            this.succeeded = succeeded.getValue();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public boolean getSucceeded() {
        return succeeded;
    }
}
