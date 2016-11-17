package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.builder.MessageBuilder;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Succeeded;
import se.umu.cs.c12mkn.message.Message;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class PostMessageHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(PostMessageHandler.class.getName());

    private final Message message;

    public PostMessageHandler(Message message) {
        this.message = message;
    }

    public EncryptedMessage setUp() {
        logger.info("Sending a message with id '" + message.getId() + "'.");
        return encryptMessage(MessageBuilder.buildMessage(message).toByteArray());
    }

    public boolean handleResponse(EncryptedMessage response) {
        try {
            Succeeded succeeded = Succeeded.parseFrom(decryptMessage(response));
            if (succeeded.getValue())
                logger.info("Server stored message with id '" +
                        message.getId() + "'.");
            return succeeded.getValue();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return false;
    }
}
