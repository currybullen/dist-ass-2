package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.client.Session;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.TopicList;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class ListTopicsHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListTopicsHandler.class.getName());

    private List<String> topics;

    public se.umu.cs.c12mkn.grpc.Session setUp() {
        logger.info("Requesting a topic list.");
        return MessageBuilder.buildSessionMessage(Session.getInstance().getID());
    }

    public void handleResponse(EncryptedMessage response) {
        try {
            TopicList topicList = TopicList.parseFrom(decryptMessage(response));
            topics = toStringList(topicList.getTopicsList());
            logger.info("Received a topic list of length " + topics.size() + ".");
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public List<String> getTopics() {
        return topics;
    }
}
