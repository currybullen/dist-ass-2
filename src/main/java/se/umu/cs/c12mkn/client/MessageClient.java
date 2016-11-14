package se.umu.cs.c12mkn.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import se.umu.cs.c12mkn.grpc.*;

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
        SignedDHResponse signedDHResponse = blockingStub.
                dHKeyExchange(messageBuilder.buildDHParameterMessage());
    }

    public static void main(String[] args) {
        try {
            new MessageClient(args[0], Integer.parseInt(args[1])).performDHKeyExchange();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

