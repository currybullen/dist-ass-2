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

    public static DHParameters buildDHParametersMessage(BigInteger modulus,
                                                        BigInteger base,
                                                        PublicKey publicKey) {
        return DHParameters.newBuilder().setModulus(toByteString(modulus))
                .setBase(toByteString(base))
                .setPublicKey(toByteString(publicKey))
                .build();
    }

//    public static DHParameters buildDHParameterMessage(String algorithm) {
//        SessionInfo sessionInfo = SessionInfo.getInstance();
//        return DHParameters.newBuilder()
//                .setModulus(toByteString(sessionInfo.getDHModulus()))
//                .setBase(toByteString(sessionInfo.getDHBase()))
//                .setPublicKey(toByteString(sessionInfo.getDHPublicKey()))
//                .setAlgorithm(algorithm)
//                .build();
//    }

    public static EncryptedMessage buildUsernameMessage(String username) {
        SessionInfo sessionInfo = SessionInfo.getInstance();
        Username message = Username.newBuilder().setValue(username).build();
        return buildAESEncryptedMessage(message.toByteArray());
    }

    public static EncryptedMessage buildAuthResponseMessage(String username, String challenge, String answer) {
        AuthResponse message = AuthResponse.newBuilder().setUsername(username)
                .setChallenge(challenge)
                .setAnswer(answer)
                .build();
        return buildAESEncryptedMessage(message.toByteArray());
    }

    private static EncryptedMessage buildAESEncryptedMessage(byte[] data) {
        SessionInfo sessionInfo = SessionInfo.getInstance();
        byte[] iv = Crypt.generateIV();
        ByteString encryptedData = ByteString.copyFrom(Crypt.encryptAES(data, sessionInfo.getSecretKey(), iv));
        return EncryptedMessage.newBuilder().setContents(encryptedData)
                .setSession(sessionInfo.getID())
                .setIv(ByteString.copyFrom(iv))
                .build();
    }

    private static ByteString toByteString(BigInteger data) {
        return ByteString.copyFrom(data.toByteArray());
    }

    private static ByteString toByteString(PublicKey publicKey) {
        return ByteString.copyFrom(publicKey.getEncoded());
    }
}
