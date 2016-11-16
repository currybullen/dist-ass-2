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
    public static DHParameters buildDHParametersMessage(BigInteger modulus,
                                                        BigInteger base,
                                                        PublicKey publicKey) {
        return DHParameters.newBuilder().setModulus(toByteString(modulus))
                .setBase(toByteString(base))
                .setPublicKey(toByteString(publicKey))
                .build();
    }

    public static Username buildUsernameMessage(String username) {
        return Username.newBuilder().setValue(username).build();
    }
    public static EncryptedMessage buildEncryptedMessage(byte[] contents,
                                                         String session,
                                                         String algorithm,
                                                         byte[] iv) {
        return EncryptedMessage.newBuilder().setContents(toByteString(contents))
                .setSession(session)
                .setAlgorithm(algorithm)
                .setIv(toByteString(iv))
                .build();
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
