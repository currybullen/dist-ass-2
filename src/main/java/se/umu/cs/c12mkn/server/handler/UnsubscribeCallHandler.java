package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.SubscriberInfo;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.database.Database;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class UnsubscribeCallHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(UnsubscribeCallHandler.class.getName());

    public EncryptedMessage handle(EncryptedMessage request) {
        try {
            SubscriberInfo subscriberInfo = SubscriberInfo.parseFrom(decryptMessage(request));
            logger.info("Received unsubscribe request for user " + subscriberInfo.getUsername() + " and topic " + subscriberInfo.getTopic() + ".");
            boolean succeeded = Database.getInstance().removeSubscriber(subscriberInfo.getTopic(),
                    subscriberInfo.getUsername());
            return encryptMessage(MessageBuilder.buildSucceededMessage(succeeded).toByteArray(), request.getSession());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return null;
    }
}
