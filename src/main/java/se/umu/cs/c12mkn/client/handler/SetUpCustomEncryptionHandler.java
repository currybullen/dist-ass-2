package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.SessionInfo;
import se.umu.cs.c12mkn.client.builder.MessageBuilder;
import se.umu.cs.c12mkn.grpc.Empty;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Session;

/**
 * Created by c12mkn on 11/17/16.
 */
public class SetUpCustomEncryptionHandler extends CallHandler {
    public Empty setUp() {
        return MessageBuilder.buildEmpty();
    }

    public boolean handleResponse(EncryptedMessage response) {
        try {
            Session session = Session.parseFrom(decryptMessage(response));
            SessionInfo.getInstance().setAlgorithm("custom");
            SessionInfo.getInstance().setID(session.getValue());
            return true;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return false;
    }
}
