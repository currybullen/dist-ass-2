package se.umu.cs.c12mkn.server.service;

import io.grpc.stub.StreamObserver;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.handler.*;
import se.umu.cs.c12mkn.server.security.Sessions;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/12/16.
 */
public class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
    public MessageService() {
        super();
    }

    @Override
    public void dHKeyExchange(DHParameters dhParameters, StreamObserver<SignedDHResponse> responseObserver) {
        SignedDHResponse response = new DHKeyExchangeHandler().handle(dhParameters);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void initAuth(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        EncryptedMessage response = new InitAuthHandler().handle(encryptedMessage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void authenticate(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        EncryptedMessage response = new AuthenticateHandler().handle(encryptedMessage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void postMessage(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        PostMessageHandler handler = new PostMessageHandler();
        EncryptedMessage response = handler.handle(encryptedMessage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listMessages(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        ListMessagesHandler handler = new ListMessagesHandler();
        EncryptedMessage response = handler.handle(encryptedMessage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listMessagesWithTimestamps(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        ListMessagesWithTimestampsHandler handler = new ListMessagesWithTimestampsHandler();
        EncryptedMessage response = handler.handle(encryptedMessage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void retrieveMessage(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        RetrieveMessageHandler handler = new RetrieveMessageHandler();
        EncryptedMessage response = handler.handle(encryptedMessage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listTopics(Session session, StreamObserver<EncryptedMessage> responseObserver) {
        ListTopicsHandler handler = new ListTopicsHandler();
        EncryptedMessage response = handler.handle(session);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void subscribe(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        SubscribeHandler handler = new SubscribeHandler();
        EncryptedMessage response = handler.handle(encryptedMessage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void unsubscribe(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        UnsubscribeHandler handler = new UnsubscribeHandler();
        EncryptedMessage response = handler.handle(encryptedMessage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listSubscribers(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        ListSubscribersHandler handler = new ListSubscribersHandler();
        EncryptedMessage response = handler.handle(encryptedMessage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listNodes(Session session, StreamObserver<EncryptedMessage> responseObserver) {
        ListNodesHandler handler = new ListNodesHandler();
        EncryptedMessage response = handler.handle(session);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void rSAKeyExchange(RSARequest rsaRequest, StreamObserver<Session> responseObserver) {
        RSAKeyExchangeHandler handler = new RSAKeyExchangeHandler();
        Session response = handler.handle(rsaRequest);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
