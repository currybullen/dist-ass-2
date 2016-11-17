package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.builder.MessageBuilder;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Message;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class RetrieveMessageHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(RetrieveMessageHandler.class.getName());

    private final String id;
    private se.umu.cs.c12mkn.message.Message message;

    public RetrieveMessageHandler(String id) {
        this.id = id;
    }

    public EncryptedMessage setUp() {
        logger.info("Requesting message with id '" + id + "'.");
        return encryptMessage(MessageBuilder.buildIDMessage(id).toByteArray());
    }

    public boolean handleResponse(EncryptedMessage response) {
        try {
            logger.info("Retrieved message with id' " + id + "'.");
            Message message = Message.parseFrom(decryptMessage(response));
            this.message = convertMessage(message);
            return true;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return false;
    }

    public se.umu.cs.c12mkn.message.Message getMessage() {
        return message;
    }

    private se.umu.cs.c12mkn.message.Message convertMessage(Message message) {
        return new se.umu.cs.c12mkn.message.Message(message.getId(),
                message.getTimestamp(),
                message.getSender(),
                message.getRecipient(),
                message.getTopic(),
                message.getContent(),
                message.getAttachments().toByteArray());
    }
}
