package se.umu.cs.c12mkn.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import se.umu.cs.c12mkn.service.MessageService;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/11/16.
 */
public class MessageServer {
    private static final Logger logger = Logger.getLogger(MessageServer.class.getName());

    private final int port;
    private final Server server;

    public MessageServer(int port) throws IOException {
        this.port = port;
        server = ServerBuilder.forPort(port).addService(new MessageService()).build();
    }

}
