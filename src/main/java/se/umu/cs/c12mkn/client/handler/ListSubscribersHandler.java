package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.SubscriberList;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class ListSubscribersHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListSubscribersHandler.class.getName());

    private String topic;
    private List<String> subscribers;

    public ListSubscribersHandler(String topic) {
        this.topic = topic;
    }

    public EncryptedMessage setUp() {
        logger.info("Requesting subscriber list for topic '" + topic + "'.");
        return encryptMessage(MessageBuilder.buildTopicMessage(topic).toByteArray());
    }

    public boolean handleResponse(EncryptedMessage response) {
        try {
            SubscriberList subscriberList = SubscriberList.parseFrom(decryptMessage(response));
            subscribers = toStringList(subscriberList.getUsernameList());
            logger.info("Received a subscriber list for topic '" +
                    topic + "' of length "  + subscribers.size() + ".");
            return true;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> getSubscribers() {
        return subscribers;
    }
}
