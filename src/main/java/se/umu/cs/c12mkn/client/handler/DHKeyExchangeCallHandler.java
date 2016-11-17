package se.umu.cs.c12mkn.client.handler;

import se.umu.cs.c12mkn.client.SessionInfo;
import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.client.security.Verify;
import se.umu.cs.c12mkn.grpc.DHParameters;
import se.umu.cs.c12mkn.grpc.DHResponse;
import se.umu.cs.c12mkn.grpc.SignedDHResponse;
import se.umu.cs.c12mkn.shared.security.DHKeyExchange;

import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class DHKeyExchangeCallHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(DHKeyExchangeCallHandler.class.getName());

    private PrivateKey privateKey;

    public DHParameters setUp() {
        DHParameterSpec dhParameterSpec = DHKeyExchange.generateParameters();
        BigInteger modulus = dhParameterSpec.getP();
        BigInteger base = dhParameterSpec.getG();
        KeyPair keyPair = DHKeyExchange.generateKeyPair(modulus, base);
        privateKey = keyPair.getPrivate();
        return MessageBuilder.buildDHParametersMessage(modulus, base, keyPair.getPublic());
    }

    public void handleResponse(SignedDHResponse signedDHResponse) {
        logger.info("DH exchange response received, verifying sender.");
        DHResponse dhResponse = signedDHResponse.getDhResponse();
        byte[] sign = signedDHResponse.getSign().toByteArray();
        if (verify(dhResponse.toByteArray(), sign)) {
            logger.info("Sender verified, creating and saving secret key.");
            byte[] publicKey = dhResponse.getPublicKey().toByteArray();
            SecretKey secretKey = DHKeyExchange.generateSecretKey(privateKey,
                    publicKey);
            SessionInfo.getInstance().setID(dhResponse.getSession());
            SessionInfo.getInstance().setSecretKey(secretKey);
        } else {
            logger.info("Sender could not be verified!");
        }
    }

    private boolean verify(byte[] data, byte[] signature) {
        return Verify.verify(data, signature, SessionInfo.getInstance().getServerPublicSignKey());
    }
}
