package se.umu.cs.c12mkn.server;

import com.google.protobuf.ByteString;
import se.umu.cs.c12mkn.grpc.*;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by c12mkn on 11/14/16.
 */
public class MessageBuilder {
    public static DHResponse buildDHResponse(PublicKey publicKey, String session) {
        return DHResponse.newBuilder()
                .setPublicKey(toByteString(publicKey))
                .setSession(session)
                .build();
    }

    public static SignedDHResponse buildSignedDHResponse(DHResponse dhResponse, byte[] sign) {
        return SignedDHResponse.newBuilder()
                .setDhResponse(dhResponse)
                .setSign(toByteString(sign))
                .build();
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

    public static Challenge buildChallengeMessage(String challenge) {
        return Challenge.newBuilder().setValue(challenge).build();
    }

    public static Succeeded buildSucceededMessage(boolean value) {
        return Succeeded.newBuilder().setValue(value).build();
    }

    public static MessageList buildMessageListMessage(List<String> ids) {
        return MessageList.newBuilder().addAllIds(ids).build();
    }

    public static MessageListWithTimestamps buildMessageListWithTimestampsMessage(Map<String, Long> timestamps) {
        List<MessageListWithTimestampsEntry> entries = new ArrayList<MessageListWithTimestampsEntry>();
        for (Map.Entry<String, Long> entry : timestamps.entrySet())
            entries.add(MessageListWithTimestampsEntry.newBuilder().
                    setId(entry.getKey()).
                    setTimestamp(entry.getValue()).
                    build());
        return MessageListWithTimestamps.newBuilder().addAllTimestamps(entries).build();
    }

    public static Message buildMessage(se.umu.cs.c12mkn.message.Message message) {
        if (message == null)
            return Message.newBuilder().build();
        return Message.newBuilder().setId(message.getId())
                .setSender(message.getSender())
                .setRecipient(message.getRecipient())
                .setTimestamp(message.getTimestamp())
                .setContent(message.getContent())
                .setTopic(message.getTopic())
                .setAttachments(toByteString(message.getAttachments()))
                .build();
    }

    public static TopicList buildTopicList(List<String> topics) {
        return TopicList.newBuilder().addAllTopics(topics).build();
    }

    public static SubscriberList buildSubscriberList(List<String> subscribers) {
        return SubscriberList.newBuilder().addAllUsername(subscribers).build();
    }

    public static NodeList buildNodeList(List<String> nodes) {
        return NodeList.newBuilder().addAllNodes(nodes).build();
    }

    private static ByteString toByteString(PublicKey publicKey) {
        return toByteString(publicKey.getEncoded());
    }

    private static ByteString toByteString(byte[] data) {
        return ByteString.copyFrom(data);
    }
}
