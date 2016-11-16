package se.umu.cs.c12mkn.client.handler;

import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.client.SessionInfo;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.shared.security.Crypt;

/**
 * Created by currybullen on 11/16/16.
 */
public class CallHandler {
    protected EncryptedMessage encryptMessage(byte[] data) {
        EncryptedMessage encryptedMessage = null;

        if (SessionInfo.getInstance().getAlgorithm().equals("AES")) {
            byte[] iv = Crypt.generateIV();
            byte[] encryptedData = Crypt.encryptAES(data, SessionInfo.getInstance().getSecretKey(), iv);
            encryptedMessage = MessageBuilder.buildEncryptedMessage(encryptedData,
                    SessionInfo.getInstance().getID(),
                    SessionInfo.getInstance().getAlgorithm(),
                    iv);
        }

        return encryptedMessage;
    }

    protected byte[] decryptMessage(EncryptedMessage encryptedMessage) {
        byte[] data = null;

        System.err.println("Algorithm: " + encryptedMessage.getAlgorithm());

        if (encryptedMessage.getAlgorithm().equals("AES")) {
            data = Crypt.decryptAES(encryptedMessage.getContents().toByteArray(),
                    SessionInfo.getInstance().getSecretKey(),
                    encryptedMessage.getIv().toByteArray());
        } else if (encryptedMessage.getAlgorithm().equals("RSA")) {
            //TODO: Implement RSA decryption call
        }

        return data;
    }
}
