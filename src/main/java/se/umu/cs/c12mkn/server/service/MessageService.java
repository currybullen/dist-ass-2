package se.umu.cs.c12mkn.server.service;

import io.grpc.stub.StreamObserver;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.server.security.Challenges;
import se.umu.cs.c12mkn.server.security.InvalidUserException;
import se.umu.cs.c12mkn.server.security.NoChallengesException;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/12/16.
 */
public class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
    private static final Logger logger = Logger.getLogger(MessageService.class.getName());

    @Override
    public void initAuth(Username username, StreamObserver<Challenge> responseObserver) {
        try {
            String challenge = Challenges.getInstance().getChallenge(username.getValue());
            responseObserver.onNext(Challenge.newBuilder().setValue(challenge).build());
        } catch (InvalidUserException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        } catch (NoChallengesException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }

        responseObserver.onCompleted();
    }

    @Override
    public void postMessage(EncryptedMessage encryptedMessage, StreamObserver<Empty> responseObserver) {

    }

//    @Override
//    public void postMessage(Message message, StreamObserver<Empty> responseObserver) {
//        se.umu.cs.c12mkn.message.Message messageToBeStored = new se.umu.cs.c12mkn.message.Message(message.getId(),
//                message.getTimestamp(),
//                message.getSender(),
//                message.getTopic(),
//                message.getContent(),
//                message.getAttachments().toByteArray());
//        Database.getInstance().addMessage(messageToBeStored);
//        responseObserver.onNext(Empty.getDefaultInstance());
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    public void retrieveMessage(Id id, StreamObserver<Message> responseObserver) {
//        se.umu.cs.c12mkn.message.Message messageFromStorage = Database.getInstance().getMessage(id.getValue());
//        responseObserver.onNext(Message.newBuilder().setId(messageFromStorage.getId())
//        .setTimestamp(messageFromStorage.getTimestamp())
//        .setSender(messageFromStorage.getSender())
//        .setTopic(messageFromStorage.getTopic())
//        .setContent(messageFromStorage.getContent())
//        .setAttachments(ByteString.copyFrom(messageFromStorage.getAttachments()))
//        .build());
//        responseObserver.onCompleted();
//    }
}
