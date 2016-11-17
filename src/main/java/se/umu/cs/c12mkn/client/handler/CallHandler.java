package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.ProtocolStringList;
import se.umu.cs.c12mkn.client.builder.MessageBuilder;
import se.umu.cs.c12mkn.client.SessionInfo;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.shared.security.Crypt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by currybullen on 11/16/16.
 */
public class CallHandler {
    private final SessionInfo sessionInfo;

    public CallHandler() {
        this.sessionInfo = SessionInfo.getInstance();
    }

    protected EncryptedMessage encryptMessage(byte[] data) {
        String algorithm = SessionInfo.getInstance().getAlgorithm();
        byte[] iv = null;
        byte[] encryptedData = null;
        String sessionID = sessionInfo.getID();
        if (algorithm.equals("AES")) {
            iv = Crypt.generateIV();
            encryptedData = Crypt.encryptAES(data,
                    sessionInfo.getSecretKey(), iv);
        } else if (algorithm.equals("RSA")) {
            encryptedData = Crypt.encryptRSA(data,
                    sessionInfo.getServerPublicKey());
        } else if (algorithm.equals("myAlgorithm")) {
            //TODO: Implement custom algorithm call
        } else {
            return null;
        }

        if (iv != null)
            return MessageBuilder.buildEncryptedMessage(encryptedData,
                    sessionID, algorithm, iv);
        return MessageBuilder.buildEncryptedMessage(encryptedData,
                sessionID, algorithm);
    }

    protected byte[] decryptMessage(EncryptedMessage encryptedMessage) {
        String algorithm = encryptedMessage.getAlgorithm();
        byte[] contents = encryptedMessage.getContents().toByteArray();
        byte[] iv = encryptedMessage.getIv().toByteArray();
        if (algorithm.equals("AES")) {
            return Crypt.decryptAES(contents,
                    SessionInfo.getInstance().getSecretKey(), iv);
        } else if (algorithm.equals("RSA")) {
            return Crypt.decryptRSA(contents, sessionInfo.getPrivateKey());
        } else if (algorithm.equals("myAlgorithm")) {
            //TODO Implement custom algorithm call
        }

        return null;
    }

    protected List<String> toStringList(ProtocolStringList originalList) {
        List<String> newList = new ArrayList<String>();
        for (ByteString byteString : originalList.asByteStringList())
            newList.add(byteString.toStringUtf8());
        return newList;
    }
}
