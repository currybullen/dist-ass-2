package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Id;
import se.umu.cs.c12mkn.message.Message;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.database.Database;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class RetrieveMessageHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(RetrieveMessageHandler.class.getName());

    public EncryptedMessage handle(EncryptedMessage request) {
        if (!validateSession(request.getSession()))
            return null;

        try {
            Id id = Id.parseFrom(decryptMessage(request));
            logger.info("Received a request for message with id '" +
                    id.getValue() + "'.");
            Message message = Database.getInstance().getMessage(id.getValue());
            return encryptMessage(MessageBuilder.buildMessage(message).
                    toByteArray(), request.getSession());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return null;
    }
}
