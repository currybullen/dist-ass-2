package se.umu.cs.c12mkn.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import se.umu.cs.c12mkn.client.handler.*;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.message.Message;

import java.util.List;
import java.util.Map;

/**
 * Created by currybullen on 11/12/16.
 */
public class MessageClient {
    private final MessageServiceGrpc.MessageServiceBlockingStub blockingStub;

    public MessageClient(String host, int port, byte[] publicKey) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).
                usePlaintext(true).build();
        blockingStub = MessageServiceGrpc.newBlockingStub(channel);
        SessionInfo.getInstance().setServerPublicKey(publicKey);
    }

    public boolean setUpCustomEncryption() {
        SetUpCustomEncryptionHandler handler = new SetUpCustomEncryptionHandler();
        Empty request = handler.setUp();
        EncryptedMessage response = blockingStub.setUpCustomEncryption(request);
        return handler.handleResponse(response);
    }

    public boolean rsaKeyExchange() {
        RSAKeyExchangeHandler handler = new RSAKeyExchangeHandler();
        RSARequest request = handler.setUp();
        Session response = blockingStub.rSAKeyExchange(request);
        return handler.handleResponse(response);
    }

    public boolean performDHKeyExchange() {
        DHKeyExchangeHandler handler = new DHKeyExchangeHandler();
        DHParameters request = handler.setUp();
        SignedDHResponse response = blockingStub.dHKeyExchange(request);
        return handler.handleResponse(response);
    }

    public String initAuth(String username) {
        InitAuthHandler handler = new InitAuthHandler(username);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.initAuth(request);
        handler.handleResponse(response);
        return handler.getChallenge();
    }

    public boolean authenticate(String username, String challenge, String answer) {
        AuthenticateHandler handler = new AuthenticateHandler(username, challenge, answer);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.authenticate(request);
        return handler.handleResponse(response);
    }

    public boolean postMessage(Message message) {
        PostMessageHandler handler = new PostMessageHandler(message);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.postMessage(request);
        return handler.handleResponse(response);
    }

    public List<String> listMessages(String topic) {
        ListMessagesHandler handler = new ListMessagesHandler(topic);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.listMessages(request);
        handler.handleResponse(response);
        return handler.getIDs();
    }

    public Map<String, Long> listMessagesWithTimestamps(String topic) {
        ListMessagesWithTimestampsHandler handler =
                new ListMessagesWithTimestampsHandler(topic);
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
        se.umu.cs.c12mkn.grpc.Session request = handler.setUp();
        EncryptedMessage response = blockingStub.listTopics(request);
        handler.handleResponse(response);
        return handler.getTopics();
    }

    public boolean subscribe(String username, String topic) {
        SubscribeHandler handler = new SubscribeHandler(username, topic);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.subscribe(request);
        return handler.handleResponse(response);
    }

    public boolean unsubscribe(String username, String topic) {
        UnsubscribeHandler handler = new UnsubscribeHandler(username, topic);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.unsubscribe(request);
        return handler.handleResponse(response);
    }

    public List<String> listSubscribers(String topic) {
        ListSubscribersHandler handler = new ListSubscribersHandler(topic);
        EncryptedMessage request = handler.setUp();
        EncryptedMessage response = blockingStub.listSubscribers(request);
        handler.handleResponse(response);
        return handler.getSubscribers();
    }

    public List<String> listNodes() {
        ListNodesHandler handler = new ListNodesHandler();
        se.umu.cs.c12mkn.grpc.Session request = handler.setUp();
        EncryptedMessage response = blockingStub.listNodes(request);
        handler.handleResponse(response);
        return handler.getNodes();
    }
}

