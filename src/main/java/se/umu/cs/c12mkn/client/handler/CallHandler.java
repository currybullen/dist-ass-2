package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.ProtocolStringList;
import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.client.Session;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.shared.security.Crypt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by currybullen on 11/16/16.
 */
public class CallHandler {
    protected EncryptedMessage encryptMessage(byte[] data) {
        String algorithm = Session.getInstance().getAlgorithm();
        if (algorithm.equals("AES")) {
            byte[] iv = Crypt.generateIV();
            byte[] encryptedData = Crypt.encryptAES(data,
                    Session.getInstance().getSecretKey(), iv);
            return MessageBuilder.buildEncryptedMessage(encryptedData,
                    Session.getInstance().getID(),
                    Session.getInstance().getAlgorithm(),
                    iv);
        } else if (algorithm.equals("RSA")) {
            //TODO: Implement RSA encryption call
        } else if (algorithm.equals("myAlgorithm")) {
            //TODO: Implement custom algorithm call
        }

        return null;
    }

    protected byte[] decryptMessage(EncryptedMessage encryptedMessage) {
        String algorithm = encryptedMessage.getAlgorithm();
        if (algorithm.equals("AES")) {
            return Crypt.decryptAES(encryptedMessage.getContents().toByteArray(),
                    Session.getInstance().getSecretKey(),
                    encryptedMessage.getIv().toByteArray());
        } else if (algorithm.equals("RSA")) {
            //TODO: Implement RSA decryption call
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
