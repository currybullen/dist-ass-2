package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Topic;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.database.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class ListMessagesWithTimestampsHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListMessagesWithTimestampsHandler.class.getName());

    public EncryptedMessage handle(EncryptedMessage request) {
        Map<String, Long> timestamps = new HashMap<String, Long>();

        try {
            Topic topic = Topic.parseFrom(decryptMessage(request));
            logger.info("Received timestamps list request for topic '" + topic.getValue() + "'.");
            List<String> ids = Database.getInstance().getMessagesByTopic(topic.getValue());
            for (String id : ids) {
                long timestamp = Database.getInstance().getMessage(id).getTimestamp();
                timestamps.put(id, timestamp);
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        logger.info("Returning a list of length " + timestamps.entrySet().size());
        return encryptMessage(MessageBuilder.
                buildMessageListWithTimestampsMessage(timestamps).toByteArray(),
                request.getSession());
    }
}
