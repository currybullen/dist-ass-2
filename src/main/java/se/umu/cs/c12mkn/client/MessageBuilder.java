package se.umu.cs.c12mkn.client;

import com.google.protobuf.ByteString;
import se.umu.cs.c12mkn.grpc.AuthResponse;
import se.umu.cs.c12mkn.grpc.DHParameters;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Username;
import se.umu.cs.c12mkn.shared.security.Crypt;

import java.math.BigInteger;
import java.security.PublicKey;

/**
 * Created by c12mkn on 11/14/16.
 */
public class MessageBuilder {
    private SessionInfo sessionInfo;

    public MessageBuilder() {
        sessionInfo = SessionInfo.getInstance();
    }

    public DHParameters buildDHParameterMessage(String algorithm) {
        return DHParameters.newBuilder()
                .setModulus(toByteString(sessionInfo.getDHModulus()))
                .setBase(toByteString(sessionInfo.getDHBase()))
                .setPublicKey(toByteString(sessionInfo.getDHPublicKey()))
                .setAlgorithm(algorithm)
                .build();
    }

    public EncryptedMessage buildUsernameMessage(String username) {
        Username message = Username.newBuilder().setValue(username).build();
        return buildEncryptedMessage(message.toByteArray());
    }

    public EncryptedMessage buildAuthResponseMessage(String username, String challenge, String answer) {
        AuthResponse message = AuthResponse.newBuilder().setUsername(username)
                .setChallenge(challenge)
                .setAnswer(answer)
                .build();
        return buildEncryptedMessage(message.toByteArray());
    }

    private EncryptedMessage buildEncryptedMessage(byte[] data) {
        ByteString encryptedData = ByteString.copyFrom(Crypt.encrypt(data, sessionInfo.getSecretKey()));
        return EncryptedMessage.newBuilder().setContents(encryptedData).build();
    }

    private ByteString toByteString(BigInteger data) {
        return ByteString.copyFrom(data.toByteArray());
    }

    private ByteString toByteString(PublicKey publicKey) {
        return ByteString.copyFrom(publicKey.getEncoded());
    }
}
