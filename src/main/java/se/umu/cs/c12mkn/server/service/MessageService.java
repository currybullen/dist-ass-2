package se.umu.cs.c12mkn.server.service;

import io.grpc.stub.StreamObserver;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.handler.AuthenticateCallHandler;
import se.umu.cs.c12mkn.server.handler.DHKeyExchangeCallHandler;
import se.umu.cs.c12mkn.server.handler.InitAuthCallHandler;
import se.umu.cs.c12mkn.server.handler.PostMessageHandler;
import se.umu.cs.c12mkn.server.security.Sessions;

import javax.crypto.SecretKey;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/12/16.
 */
public class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
    private static final Logger logger = Logger.getLogger(MessageService.class.getName());

    private final MessageBuilder messageBuilder;

    public MessageService() {
        super();
        messageBuilder = new MessageBuilder();
    }

    @Override
    public void dHKeyExchange(DHParameters dhParameters, StreamObserver<SignedDHResponse> responseObserver) {
        SignedDHResponse response = new DHKeyExchangeCallHandler().handle(dhParameters);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        logger.info("Responded to DH key exchange request.");
    }

    @Override
    public void initAuth(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        EncryptedMessage response = new InitAuthCallHandler().handle(encryptedMessage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void authenticate(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        EncryptedMessage response = new AuthenticateCallHandler().handle(encryptedMessage);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void postMessage(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        PostMessageHandler handler = new PostMessageHandler();
        handler.handle(encryptedMessage);
    }

    private SecretKey getSecretKey(String session) {
        return Sessions.getInstance().getSecretKey(session);
    }
}
