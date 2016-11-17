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
public class ListSubscribersHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListSubscribersHandler.class.getName());

    public EncryptedMessage handle(EncryptedMessage request) {
        if (!validateSession(request.getSession()))
            return null;

        try {
            Topic topic = Topic.parseFrom(decryptMessage(request));
            logger.info("Received request for subscriber list for topic '" +
                    topic.getValue() + "'.");
            List<String> subscribers = Database.getInstance().
                    getSubscribers(topic.getValue());
            logger.info("Returning a list of length " + subscribers.size() + ".");
            return encryptMessage(MessageBuilder.buildSubscriberList(subscribers).
                    toByteArray(), request.getSession());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return null;
    }
}
