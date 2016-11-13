package se.umu.cs.c12mkn.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import se.umu.cs.c12mkn.grpc.Challenge;
import se.umu.cs.c12mkn.grpc.MessageServiceGrpc;
import se.umu.cs.c12mkn.grpc.Username;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/12/16.
 */
public class MessageClient {
    private static final Logger logger = Logger.getLogger(MessageClient.class.getName());

//    private final ManagedChannel channel;
//    private final MessageServiceGrpc.MessageServiceBlockingStub blockingStub;

//    public MessageClient(String host, int port) {
//        //TODO Why Plaintext?
//        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
//        blockingStub = MessageServiceGrpc.newBlockingStub(channel);
//    }
//
//    public void initAuth(String username) {
//        Challenge challenge = blockingStub.initAuth(Username.newBuilder().setValue(username).build());
//        if (challenge == null)
//            logger.info("initAuth did not respond, wrong username or out of challenges maybe?");
//        logger.info("Challenge received: '" + challenge.getValue() + "'.");
//    }

    public static void main(String[] args) {
//        new MessageClient(args[0], Integer.parseInt(args[1])).initAuth("currybullen");
    }
}

