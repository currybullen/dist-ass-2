package se.umu.cs.c12mkn.server;

import com.google.protobuf.ByteString;
import se.umu.cs.c12mkn.grpc.DHResponse;
import se.umu.cs.c12mkn.grpc.SignedDHResponse;
import se.umu.cs.c12mkn.server.security.Sign;

import java.security.PublicKey;

/**
 * Created by c12mkn on 11/14/16.
 */
public class MessageBuilder {
    public SignedDHResponse buildSignedDHResponse(PublicKey publicKey, String session) {
        DHResponse dhResponse = DHResponse.newBuilder()
                .setPublicKey(toByteString(publicKey))
                .setSession(session)
                .build();
        byte[] signature = Sign.getInstance().sign(dhResponse.toByteArray());
        return SignedDHResponse.newBuilder()
                .setDhResponse(dhResponse)
                .setSign(toByteString(signature))
                .build();
    }

    private ByteString toByteString(PublicKey publicKey) {
        return toByteString(publicKey.getEncoded());
    }

    private ByteString toByteString(byte[] data) {
        return ByteString.copyFrom(data);
    }
}
