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
public class ListMessagesCallHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListMessagesCallHandler.class.getName());

    private String topic;
    private List<String> ids;

    public ListMessagesCallHandler(String topic) {
        this.topic = topic;
    }

    public EncryptedMessage setUp() {
        return encryptMessage(MessageBuilder.buildTopicMessage(topic).toByteArray());
    }

    public void handleResponse(EncryptedMessage response) {
        try {
            TopicList topicList = TopicList.parseFrom(decryptMessage(response));
            ids = toStringList(topicList.getTopicsList());
            logger.info("Received a message list of length " + ids.size() + ".");
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public List<String> getIDs() {
        return ids;
    }
}