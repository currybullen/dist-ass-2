package se.umu.cs.c12mkn.server.handler;

import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Session;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.database.Database;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class ListNodesHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListNodesHandler.class.getName());

    public EncryptedMessage handle(Session session) {
        if (!validateSession(session.getValue()))
            return null;

        List<String> nodes = new ArrayList<String>();
        try {
            nodes.add(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        logger.info("Sending node list of length " + nodes.size() + ".");
        return encryptMessage(MessageBuilder.buildNodeList(nodes).toByteArray(),
                session.getValue());
    }
}
