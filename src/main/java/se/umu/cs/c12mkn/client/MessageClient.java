package se.umu.cs.c12mkn.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import se.umu.cs.c12mkn.client.handler.*;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.message.*;
import se.umu.cs.c12mkn.message.Message;

import java.util.List;
import java.util.Map;
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

    public List<String> listMessages(String topic) {
        ListMessagesCallHandler handler = new ListMessagesCallHandler(topic);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.listMessages(request);
        handler.handleResponse(response);
        return handler.getIDs();
    }

    public Map<String, Long> listMessagesWithTimestamps(String topic) {
        ListMessagesWithTimestampsCallHandler handler =
                new ListMessagesWithTimestampsCallHandler(topic);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.listMessagesWithTimestamps(request);
        handler.handleResponse(response);
        return handler.getTimestamps();
    }

    public Message retrieveMessage(String id) {
        RetrieveMessageHandler handler = new RetrieveMessageHandler(id);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.retrieveMessage(request);
        handler.handleResponse(response);
        return handler.getMessage();
    }

    public List<String> listTopics() {
        ListTopicsHandler handler = new ListTopicsHandler();
        Session request = handler.setUp();
        EncryptedMessage response = blockingStub.listTopics(request);
        handler.handleResponse(response);
        return handler.getTopics();
    }

    public boolean subscribe(String username, String topic) {
        SubscribeCallHandler handler = new SubscribeCallHandler(username, topic);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.subscribe(request);
        handler.handleResponse(response);
        return handler.getSucceeded();
    }

    public boolean unsubscribe(String username, String topic) {
        UnsubscribeCallHandler handler = new UnsubscribeCallHandler(username, topic);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.unsubscribe(request);
        handler.handleResponse(response);
        return handler.getSucceeded();
    }

    public List<String> listSubscribers(String topic) {
        ListSubscribersCallHandler handler = new ListSubscribersCallHandler(topic);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.listSubscribers(request);
        handler.handleResponse(response);
        return handler.getSubscribers();
    }

    public List<String> listNodes() {
        ListNodesCallHandler handler = new ListNodesCallHandler();
        Session request = handler.setUp();
        EncryptedMessage response = blockingStub.listNodes(request);
        handler.handleResponse(response);
        return handler.getNodes();
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
            messageClient.postMessage(message);
            se.umu.cs.c12mkn.message.Message message2 = new Message("2",6,"micke","anna","bajs1","hehe","tjoho".getBytes());
            messageClient.postMessage(message2);
            messageClient.listMessages("bajs");
            messageClient.listMessagesWithTimestamps("bajs").get("2");
            messageClient.retrieveMessage("2");
            messageClient.listTopics();
            messageClient.subscribe("currybullen", "bajs");
            messageClient.unsubscribe("currybullen", "bajs");
            messageClient.subscribe("paprikafix", "bajs");
            messageClient.listSubscribers("bajs");
            messageClient.listNodes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

