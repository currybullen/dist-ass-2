package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.ByteString;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Succeeded;
import se.umu.cs.c12mkn.message.Message;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.security.Sessions;
import se.umu.cs.c12mkn.shared.security.Crypt;

/**
 * Created by currybullen on 11/16/16.
 */
public class CallHandler {
    private final Sessions sessions;

    public CallHandler() {
        sessions = Sessions.getInstance();
    }

    protected byte[] decryptMessage(EncryptedMessage encryptedMessage) {
        String session = encryptedMessage.getSession();
        String algorithm = sessions.getAlgorithm(session);
        if (algorithm.equals("AES")) {
            return Crypt.decryptAES(encryptedMessage.getContents().toByteArray(),
                    sessions.getSecretKey(session),
                    encryptedMessage.getIv().toByteArray());
        } else if (algorithm.equals("RSA")) {
            //TODO: RSA decrypt call
        } else if (algorithm.equals("customAlgorithm")) {
            //TODO: Custom algorithm decrypt call
        }

        return null;
    }

    protected EncryptedMessage encryptMessage(byte[] data, String session) {
        byte[] iv = null;
        byte[] contents = null;
        String algorithm = sessions.getAlgorithm(session);
        if (algorithm.equals("AES")) {
            iv = Crypt.generateIV();
            contents = Crypt.encryptAES(data, sessions.getSecretKey(session), iv);
        } else if (algorithm.equals("RSA")) {
            //TODO: RSA encrypt call
        } else if (algorithm.equals("customAlgorithm")) {
            //TODO: Custom algoritmh encrypt call
        } else {
            return null;
        }

        if (iv != null)
            return MessageBuilder.buildEncryptedMessage(contents, session, algorithm, iv);
        return MessageBuilder.buildEncryptedMessage(contents, session, algorithm);
    }

    protected Message convertMessage(se.umu.cs.c12mkn.grpc.Message message) {
        return new Message(message.getId(),
                message.getTimestamp(),
                message.getSender(),
                message.getRecipient(),
                message.getTopic(),
                message.getContent(),
                message.getAttachments().toByteArray());
    }

    protected EncryptedMessage buildEncryptedSuccessMessage(
            boolean succeeded, String session) {
        Succeeded message = MessageBuilder.buildSucceededMessage(succeeded);
        return encryptMessage(message.toByteArray(), session);
    }
}
