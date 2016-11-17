package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.TopicList;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class ListMessagesHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListMessagesHandler.class.getName());

    private final String topic;
    private List<String> ids;

    public ListMessagesHandler(String topic) {
        this.topic = topic;
    }

    public EncryptedMessage setUp() {
        return encryptMessage(MessageBuilder.buildTopicMessage(topic).toByteArray());
    }

    public boolean handleResponse(EncryptedMessage response) {
        try {
            TopicList topicList = TopicList.parseFrom(decryptMessage(response));
            ids = toStringList(topicList.getTopicsList());
            logger.info("Received a message list of length " + ids.size() + ".");
            return true;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> getIDs() {
        return ids;
    }
}
