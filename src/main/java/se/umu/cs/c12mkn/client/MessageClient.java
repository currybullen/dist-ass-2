package se.umu.cs.c12mkn.client;

import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import se.umu.cs.c12mkn.client.security.Verify;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.shared.security.Crypt;
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
            SessionInfo.getInstance().setID(dhResponse.getSession());
            SessionInfo.getInstance().setSecretKey(secretKey);
        } else {
            logger.info("Sender could not be verified!");
        }
    }

    public void initAuth(String username) {
        EncryptedMessage encryptedRequest = messageBuilder.buildUsernameMessage(username);
        EncryptedMessage encryptedResponse = blockingStub.initAuth(encryptedRequest);
        byte[] decryptedResponse = Crypt.decrypt(encryptedResponse.toByteArray(), SessionInfo.getInstance().getSecretKey());
        try {
            Challenge challenge = Challenge.parseFrom(decryptedResponse);
            logger.info("Challenge received: '" + challenge.getValue() + "'.");
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            MessageClient messageClient = new MessageClient(args[0], Integer.parseInt(args[1]));
            SessionInfo.getInstance().setServerPublicSignKey(args[2]);
            messageClient.performDHKeyExchange("AES");
            messageClient.initAuth("currybullen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

