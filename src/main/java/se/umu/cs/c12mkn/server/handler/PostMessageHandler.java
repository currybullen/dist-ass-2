package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Message;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.database.Database;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class PostMessageHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(PostMessageHandler.class.getName());

    public EncryptedMessage handle(EncryptedMessage request) {
        try {
            Message message = Message.parseFrom(decryptMessage(request));
            logger.info("Message with id '" + message.getId() + "' received, storing.");
            Database.getInstance().addMessage(convertMessage(message));
            return encryptMessage(MessageBuilder.buildSucceededMessage(true).toByteArray(),
                    request.getSession());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return encryptMessage(MessageBuilder.buildSucceededMessage(false).toByteArray(),
                request.getSession());
    }
}
