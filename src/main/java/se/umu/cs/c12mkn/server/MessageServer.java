package se.umu.cs.c12mkn.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import se.umu.cs.c12mkn.server.security.Sign;
import se.umu.cs.c12mkn.server.service.MessageService;

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

    public void start() throws IOException {
        server.start();
        logger.info("Server started on port " + port + ".");
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null)
            server.awaitTermination();
    }

    public static void main(String[] args) {
        try {
            Sign.getInstance().setPrivateKey(args[1]);
            MessageServer messageServer = new MessageServer(Integer.parseInt(args[0]));
            messageServer.start();
            messageServer.blockUntilShutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
