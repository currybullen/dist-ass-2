package se.umu.cs.c12mkn.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import se.umu.cs.c12mkn.client.security.Verify;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.shared.security.DHKeyExchange;

import javax.crypto.SecretKey;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/12/16.
 */
public class MessageClient {
    private static final Logger logger = Logger.getLogger(MessageClient.class.getName());

    private final ManagedChannel channel;
    private final MessageServiceGrpc.MessageServiceBlockingStub blockingStub;
    private final MessageBuilder messageBuilder;

    public MessageClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        blockingStub = MessageServiceGrpc.newBlockingStub(channel);
        messageBuilder = new MessageBuilder();
    }

    public void performDHKeyExchange(String algorithm) {
        SignedDHResponse signedDHResponse = blockingStub.
                dHKeyExchange(messageBuilder.buildDHParameterMessage(algorithm));
        logger.info("DH exchange request sent, using algorithm '" + algorithm + "'.");
        DHResponse dhResponse = signedDHResponse.getDhResponse();
        logger.info("DH exchange response received, verifying sender.");
        boolean verified = Verify.verify(dhResponse.toByteArray(),
                signedDHResponse.getSign().toByteArray());
        if (verified) {
            logger.info("Sender verified, creating and saving secret key.");
            SecretKey secretKey = DHKeyExchange.generateSecretKey(SessionInfo.getInstance().getDHPrivateKey(),
                    dhResponse.getPublicKey().toByteArray(), algorithm);
            SessionInfo.getInstance().setSecretKey(secretKey);
        } else {
            logger.info("Sender could not be verified!");
        }
    }

    public static void main(String[] args) {
        try {
            new MessageClient(args[0], Integer.parseInt(args[1])).
                    performDHKeyExchange("AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

