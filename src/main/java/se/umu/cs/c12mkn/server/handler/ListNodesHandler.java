package se.umu.cs.c12mkn.server.handler;

import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Session;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.database.Database;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class ListNodesHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListNodesHandler.class.getName());

    public EncryptedMessage handle(Session session) {
        List<String> nodes = Database.getInstance().getNodes();
        logger.info("Sending node list of length " + nodes.size() + ".");
        return encryptMessage(MessageBuilder.buildNodeList(nodes).toByteArray(),
                session.getValue());
    }
}
