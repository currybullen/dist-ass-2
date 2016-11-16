package se.umu.cs.c12mkn.server.handler;

import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Session;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class ListTopicsCallHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListTopicsCallHandler.class.getName());

    public EncryptedMessage handle(Session session) {
        logger.info("Received request for topic list");
        List<String> topics = Database.getInstance().getTopics();
        if (topics == null)
            topics = new ArrayList<String>();
        logger.info("Returning a topic list of length " + topics.size() + ".");
        return encryptMessage(MessageBuilder.buildTopicList(topics).toByteArray(), session.getValue());
    }
}
