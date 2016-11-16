package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.ByteString;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.server.security.Sessions;
import se.umu.cs.c12mkn.shared.security.Crypt;

/**
 * Created by currybullen on 11/16/16.
 */
public class CallHandler {
    protected byte[] decryptMessage(EncryptedMessage encryptedMessage) {
        byte[] data = null;

        if (encryptedMessage.getAlgorithm().equals("AES")) {
            data = Crypt.decryptAES(encryptedMessage.getContents().toByteArray(),
                    Sessions.getInstance().getSecretKey(encryptedMessage.getSession()),
                    encryptedMessage.getIv().toByteArray());
        } else if (encryptedMessage.getAlgorithm().equals("RSA")) {
            //TODO: Implement RSA decryption call
        }

        return data;
    }

    protected EncryptedMessage encryptMessage(byte[] data, String session, String algorithm) {
        EncryptedMessage encryptedMessage = null;

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
}
