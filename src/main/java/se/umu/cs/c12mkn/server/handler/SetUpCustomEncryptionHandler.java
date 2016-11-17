package se.umu.cs.c12mkn.server.handler;

import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.security.Sessions;

/**
 * Created by c12mkn on 11/17/16.
 */
public class SetUpCustomEncryptionHandler extends CallHandler {
    public EncryptedMessage handle() {
        String session = Sessions.getInstance().createSession("custom", null);

        return encryptMessage(MessageBuilder.
                buildSessionMessage(session).toByteArray(), session);
    }
}
