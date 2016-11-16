package se.umu.cs.c12mkn.client;

import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import se.umu.cs.c12mkn.client.handler.DHKeyExchangeCallHandler;
import se.umu.cs.c12mkn.client.handler.InitAuthCallHandler;
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

    public void performDHKeyExchange() {
        DHKeyExchangeCallHandler handler = new DHKeyExchangeCallHandler();
        DHParameters request = handler.setUp();
        logger.info("DH exchange request sent.");
        SignedDHResponse response = blockingStub.dHKeyExchange(request);
        handler.handleResponse(response);
    }

    public String initAuth(String username) {
        InitAuthCallHandler handler = new InitAuthCallHandler(username);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.initAuth(request);
        handler.handleResponse(response);
        return handler.getChallenge();
    }

    public static void main(String[] args) {
        try {
            MessageClient messageClient = new MessageClient(args[0], Integer.parseInt(args[1]));
            SessionInfo.getInstance().setServerPublicSignKey(args[2]);
            messageClient.performDHKeyExchange();
            messageClient.initAuth("currybullen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

