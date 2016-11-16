package se.umu.cs.c12mkn.client.handler;

import se.umu.cs.c12mkn.client.SessionInfo;
import se.umu.cs.c12mkn.client.MessageBuilder;
import se.umu.cs.c12mkn.client.security.Verify;
import se.umu.cs.c12mkn.grpc.DHParameters;
import se.umu.cs.c12mkn.grpc.DHResponse;
import se.umu.cs.c12mkn.grpc.SignedDHResponse;
import se.umu.cs.c12mkn.shared.security.DHKeyExchange;

import javax.crypto.SecretKey;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class DHKeyExchangeCallHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(DHKeyExchangeCallHandler.class.getName());

    public DHParameters setUp() {
        SessionInfo sessionInfo = SessionInfo.getInstance();
        return MessageBuilder.buildDHParametersMessage(sessionInfo.getDHModulus(),
                sessionInfo.getDHBase(),
                sessionInfo.getDHPublicKey());
    }

    public void handleResponse(SignedDHResponse signedDHResponse) {
        logger.info("DH exchange response received, verifying sender.");
        DHResponse dhResponse = signedDHResponse.getDhResponse();
        if (Verify.verify(dhResponse.toByteArray(),
                signedDHResponse.getSign().toByteArray())) {
            logger.info("Sender verified, creating and saving secret key.");
            SecretKey secretKey = DHKeyExchange.generateSecretKey(
                    SessionInfo.getInstance().getDHPrivateKey(),
                    dhResponse.getPublicKey().toByteArray());
            SessionInfo.getInstance().setID(dhResponse.getSession());
            SessionInfo.getInstance().setSecretKey(secretKey);
        } else {
            logger.info("Sender could not be verified!");
        }
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

//    public void performDHKeyExchange(String algorithm) {
//        SignedDHResponse signedDHResponse = blockingStub.
//                dHKeyExchange(messageBuilder.buildDHParameterMessage(algorithm));
//        logger.info("DH exchange request sent, using algorithm '" + algorithm + "'.");
//        DHResponse dhResponse = signedDHResponse.getDhResponse();
//        logger.info("DH exchange response received, verifying sender.");
//        boolean verified = Verify.verify(dhResponse.toByteArray(),
//                signedDHResponse.getSign().toByteArray());
//        if (verified) {
//            logger.info("Sender verified, creating and saving secret key.");
//            SecretKey secretKey = DHKeyExchange.generateSecretKey(SessionInfo.getInstance().getDHPrivateKey(),
//                    dhResponse.getPublicKey().toByteArray(), algorithm);
//            SessionInfo.getInstance().setID(dhResponse.getSession());
//            SessionInfo.getInstance().setSecretKey(secretKey);
//        } else {
//            logger.info("Sender could not be verified!");
//        }
//    }
}
