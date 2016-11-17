package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Topic;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.database.Database;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class ListMessagesHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListMessagesHandler.class.getName());

    public EncryptedMessage handle(EncryptedMessage request) {
        try {
            Topic topic = Topic.parseFrom(decryptMessage(request));
            logger.info("Received message list request for topic '" + topic.getValue() + "'.");
            List<String> ids = Database.getInstance().getMessagesByTopic(topic.getValue());
            logger.info("Returning a list of length " + ids.size() + ".");
            return encryptMessage(MessageBuilder.buildMessageListMessage(ids).toByteArray(),
                    request.getSession());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return null;
    }
}
