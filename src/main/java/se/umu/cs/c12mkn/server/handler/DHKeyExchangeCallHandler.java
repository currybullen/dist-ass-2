package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.ByteString;
import se.umu.cs.c12mkn.grpc.DHParameters;
import se.umu.cs.c12mkn.grpc.DHResponse;
import se.umu.cs.c12mkn.grpc.SignedDHResponse;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.security.Sessions;
import se.umu.cs.c12mkn.server.security.Sign;
import se.umu.cs.c12mkn.shared.security.Crypt;
import se.umu.cs.c12mkn.shared.security.DHKeyExchange;

import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHKey;
import java.security.KeyPair;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class DHKeyExchangeCallHandler {
    private static final Logger logger = Logger.getLogger(DHKeyExchangeCallHandler.class.getName());

    public SignedDHResponse handle(DHParameters dhParameters) {
        logger.info("DH key exchange request received.");
        KeyPair keyPair = DHKeyExchange.generateKeyPair(
                dhParameters.getModulus().toByteArray(),
                dhParameters.getBase().toByteArray());
        SecretKey secretKey = DHKeyExchange.generateSecretKey(
                keyPair.getPrivate(), dhParameters.getPublicKey().toByteArray());
        String session = Sessions.getInstance().createSession("AES", secretKey);
        DHResponse dhResponse = MessageBuilder.buildDHResponse(keyPair.getPublic(), session);
        return MessageBuilder.buildSignedDHResponse(dhResponse, sign(dhResponse.toByteArray()));
    }

    private byte[] sign(byte[] data) {
        return Sign.getInstance().sign(data);
    }
}
