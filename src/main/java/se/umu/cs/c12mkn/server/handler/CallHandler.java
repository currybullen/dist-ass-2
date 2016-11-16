package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.ByteString;
import se.umu.cs.c12mkn.client.SessionInfo;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.message.Message;
import se.umu.cs.c12mkn.server.security.Sessions;
import se.umu.cs.c12mkn.shared.security.Crypt;

/**
 * Created by currybullen on 11/16/16.
 */
public class CallHandler {
    protected byte[] decryptMessage(EncryptedMessage encryptedMessage) {
        byte[] data = null;
        Sessions sessions = Sessions.getInstance();
        String session = encryptedMessage.getSession();
        String algorithm = sessions.getAlgorithm(encryptedMessage.getSession());

        if (algorithm.equals("AES")) {
            data = Crypt.decryptAES(encryptedMessage.getContents().toByteArray(),
                    sessions.getSecretKey(session),
                    encryptedMessage.getIv().toByteArray());
        } else if (algorithm.equals("RSA")) {

        }

        return data;
    }

    protected EncryptedMessage encryptMessage(byte[] data, String session) {
        EncryptedMessage encryptedMessage = null;

        String algorithm = Sessions.getInstance().getAlgorithm(session);
        if (algorithm.equals("AES")) {
            byte[] iv = Crypt.generateIV();
            byte[] encryptedData = Crypt.encryptAES(data, Sessions.getInstance().getSecretKey(session), iv);
            //TODO: Move building code to MessageBuilder?
            encryptedMessage = EncryptedMessage.newBuilder().setContents(ByteString.copyFrom(encryptedData))
                    .setAlgorithm("AES")
                    .setSession(session)
                    .setIv(ByteString.copyFrom(iv))
                    .build();
        } else if (algorithm.equals("RSA")) {
            //TODO: Implement RSA encryption call
        }

        return encryptedMessage;
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
}
