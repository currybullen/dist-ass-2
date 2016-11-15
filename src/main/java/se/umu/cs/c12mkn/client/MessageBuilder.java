package se.umu.cs.c12mkn.client;

import com.google.protobuf.ByteString;
import se.umu.cs.c12mkn.grpc.DHParameters;

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

    private ByteString toByteString(BigInteger data) {
        return ByteString.copyFrom(data.toByteArray());
    }

    private ByteString toByteString(PublicKey publicKey) {
        return ByteString.copyFrom(publicKey.getEncoded());
    }
}
