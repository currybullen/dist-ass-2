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
public class ListSubscribersCallHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListSubscribersCallHandler.class.getName());

    private String topic;
    private List<String> subscribers;

    public ListSubscribersCallHandler(String topic) {
        this.topic = topic;
    }

    public EncryptedMessage setUp() {
        return encryptMessage(MessageBuilder.buildTopicMessage(topic).toByteArray());
    }

    public void handleResponse(EncryptedMessage response) {
        try {
            logger.info("Requesting subscriber list for topic '" + topic + "'.");
            SubscriberList subscriberList = SubscriberList.parseFrom(decryptMessage(response));
            this.subscribers = toStringList(subscriberList.getUsernameList());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSubscribers() {
        return subscribers;
    }
}
