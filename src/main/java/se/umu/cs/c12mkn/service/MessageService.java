package se.umu.cs.c12mkn.service;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import se.umu.cs.c12mkn.database.Database;
import se.umu.cs.c12mkn.grpc.Empty;
import se.umu.cs.c12mkn.grpc.Id;
import se.umu.cs.c12mkn.grpc.Message;
import se.umu.cs.c12mkn.grpc.MessageServiceGrpc;

/**
 * Created by currybullen on 11/12/16.
 */
public class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
    @Override
    public void postMessage(Message message, StreamObserver<Empty> responseObserver) {
        se.umu.cs.c12mkn.message.Message messageToBeStored = new se.umu.cs.c12mkn.message.Message(message.getId(),
                message.getTimestamp(),
                message.getSender(),
                message.getTopic(),
                message.getContent(),
                message.getAttachments().toByteArray());
        Database.getInstance().addMessage(messageToBeStored);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void retrieveMessage(Id id, StreamObserver<Message> responseObserver) {
        se.umu.cs.c12mkn.message.Message messageFromStorage = Database.getInstance().getMessage(id.getValue());
        responseObserver.onNext(Message.newBuilder().setId(messageFromStorage.getId())
        .setTimestamp(messageFromStorage.getTimestamp())
        .setSender(messageFromStorage.getSender())
        .setTopic(messageFromStorage.getTopic())
        .setContent(messageFromStorage.getContent())
        .setAttachments(ByteString.copyFrom(messageFromStorage.getAttachments()))
        .build());
    }
}
