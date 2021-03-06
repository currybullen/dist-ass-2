package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.builder.MessageBuilder;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.MessageListWithTimestamps;
import se.umu.cs.c12mkn.grpc.MessageListWithTimestampsEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class ListMessagesWithTimestampsHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListMessagesWithTimestampsHandler.class.getName());

    private final String topic;
    private Map<String, Long> timestamps;

    public ListMessagesWithTimestampsHandler(String topic) {
        this.topic = topic;
    }

    public EncryptedMessage setUp() {
        return encryptMessage(MessageBuilder.buildTopicMessage(topic).toByteArray());
    }

    public boolean handleResponse(EncryptedMessage response) {
        try {
            MessageListWithTimestamps list = MessageListWithTimestamps.
                    parseFrom(decryptMessage(response));
            logger.info("Received a message timestamp list of length " +
                    list.getTimestampsList().size() + ".");
            timestamps = toHashMap(list.getTimestampsList());
            return true;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Map<String, Long> getTimestamps() {
        return timestamps;
    }

    private Map<String, Long> toHashMap(List<MessageListWithTimestampsEntry> list) {
        Map<String, Long> timestamps = new HashMap<String, Long>();
        for (MessageListWithTimestampsEntry entry : list)
            timestamps.put(entry.getId(), entry.getTimestamp());
        return timestamps;
    }
}
