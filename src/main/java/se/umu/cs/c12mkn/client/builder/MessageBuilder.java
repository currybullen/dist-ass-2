package se.umu.cs.c12mkn.client.builder;

import com.google.protobuf.ByteString;
import se.umu.cs.c12mkn.client.SessionInfo;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.grpc.Message;

import java.math.BigInteger;
import java.security.PublicKey;

/**
 * Created by c12mkn on 11/14/16.
 */
public class MessageBuilder {
    public static Empty buildEmpty() {
        return Empty.newBuilder().build();
    }

    public static RSARequest buildRSARequestMessage(PublicKey publicKey) {
        return RSARequest.newBuilder().setPublicKey(toByteString(publicKey))
                .build();
    }

    public static DHParameters buildDHParametersMessage(
            BigInteger modulus, BigInteger base, PublicKey publicKey) {
        return DHParameters.newBuilder().setModulus(toByteString(modulus))
                .setBase(toByteString(base))
                .setPublicKey(toByteString(publicKey))
                .build();
    }

    public static Username buildUsernameMessage(String username) {
        return Username.newBuilder().setValue(username).build();
    }

    public static EncryptedMessage buildEncryptedMessage(
            byte[] contents, String session, String algorithm, byte[] iv) {
        return EncryptedMessage.newBuilder().setContents(toByteString(contents))
                .setSession(session)
                .setAlgorithm(algorithm)
                .setIv(toByteString(iv))
                .build();
    }

    public static EncryptedMessage buildEncryptedMessage(
            byte[] contents, String session, String algorithm) {
        return EncryptedMessage.newBuilder().setContents(toByteString(contents))
                .setSession(session)
                .setAlgorithm(algorithm)
                .build();
    }

    public static Message buildMessage(se.umu.cs.c12mkn.message.Message message) {
        return Message.newBuilder().setId(message.getId())
                .setSender(message.getSender())
                .setRecipient(message.getRecipient())
                .setTimestamp(message.getTimestamp())
                .setContent(message.getContent())
                .setTopic(message.getTopic())
                .setAttachments(toByteString(message.getAttachments()))
                .build();
    }

    public static AuthResponse buildAuthResponseMessage(
            String username, String challenge, String answer) {
        return AuthResponse.newBuilder().setUsername(username)
                .setChallenge(challenge)
                .setAnswer(answer)
                .build();
    }

    public static Topic buildTopicMessage(String topic) {
        return Topic.newBuilder().setValue(topic).build();
    }

    public static Id buildIDMessage(String id) {
        return Id.newBuilder().setValue(id).build();
    }

    public static Session buildSessionMessage(String session) {
        return Session.newBuilder().setValue(session).build();
    }

    public static SubscriberInfo buildSubscriberInfoMessage(
            String username, String topic) {
        return SubscriberInfo.newBuilder().setUsername(username).
                setTopic(topic).build();
    }

    private static ByteString toByteString(byte[] data) {
        return ByteString.copyFrom(data);
    }

    private static ByteString toByteString(BigInteger data) {
        return ByteString.copyFrom(data.toByteArray());
    }

    private static ByteString toByteString(PublicKey publicKey) {
        return ByteString.copyFrom(publicKey.getEncoded());
    }
}
