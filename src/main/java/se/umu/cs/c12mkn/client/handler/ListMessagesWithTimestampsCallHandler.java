package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.MessageBuilder;
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
public class ListMessagesWithTimestampsCallHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListMessagesWithTimestampsCallHandler.class.getName());

    private String topic;
    private Map<String, Long> timestamps;

    public ListMessagesWithTimestampsCallHandler(String topic) {
        this.topic = topic;
    }

    public EncryptedMessage setUp() {
        return encryptMessage(MessageBuilder.buildTopicMessage(topic).toByteArray());
    }

    public void handleResponse(EncryptedMessage response) {
        try {
            MessageListWithTimestamps list = MessageListWithTimestamps.
                    parseFrom(decryptMessage(response));
            logger.info("Received a message timestamp list of length " + list.getTimestampsList().size() + ".");
            timestamps = toHashMap(list.getTimestampsList());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
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
