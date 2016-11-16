package se.umu.cs.c12mkn.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import se.umu.cs.c12mkn.client.handler.AuthenticateCallHandler;
import se.umu.cs.c12mkn.client.handler.DHKeyExchangeCallHandler;
import se.umu.cs.c12mkn.client.handler.InitAuthCallHandler;
import se.umu.cs.c12mkn.client.handler.PostMessageHandler;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.message.*;
import se.umu.cs.c12mkn.message.Message;

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

    public void authenticate(String username, String challenge, String answer) {
        AuthenticateCallHandler handler = new AuthenticateCallHandler(username, challenge, answer);
        EncryptedMessage request = handler.setUp();
        blockingStub.authenticate(request);
    }

    public void postMessage(Message message) {
        PostMessageHandler handler = new PostMessageHandler(message);
        EncryptedMessage request = handler.setUp();
        blockingStub.postMessage(request);
    }

    public static void main(String[] args) {
        try {
            MessageClient messageClient = new MessageClient(args[0], Integer.parseInt(args[1]));
            SessionInfo.getInstance().setServerPublicSignKey(args[2]);
            SessionInfo.getInstance().setAlgorithm("AES");
            messageClient.performDHKeyExchange();
            messageClient.initAuth("currybullen");
            messageClient.authenticate("currybullen", "nkSW4rs5", "ZfDPxY5Y");
            se.umu.cs.c12mkn.message.Message message = new Message("1",5,"micke","anna","bajs","hehe","tjoho".getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

