package se.umu.cs.c12mkn.server;

import com.google.protobuf.ByteString;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.server.security.Sessions;
import se.umu.cs.c12mkn.server.security.Sign;
import se.umu.cs.c12mkn.shared.security.Crypt;

import java.security.PublicKey;
import java.util.List;

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

    public static Challenge buildChallengeMessage(String challenge) {
        return Challenge.newBuilder().setValue(challenge).build();
    }

    public static Succeeded buildSucceededMessage(boolean value) {
        return Succeeded.newBuilder().setValue(value).build();
    }

    public static MessageList buildMessageListMessage(List<String> ids) {
        return MessageList.newBuilder().addAllIds(ids).build();
    }

    private static ByteString toByteString(PublicKey publicKey) {
        return toByteString(publicKey.getEncoded());
    }

    private static ByteString toByteString(byte[] data) {
        return ByteString.copyFrom(data);
    }
}
