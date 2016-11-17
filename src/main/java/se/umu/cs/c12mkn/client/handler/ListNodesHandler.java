package se.umu.cs.c12mkn.client.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.client.builder.MessageBuilder;
import se.umu.cs.c12mkn.client.SessionInfo;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.NodeList;
import se.umu.cs.c12mkn.grpc.Session;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class ListNodesHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(ListNodesHandler.class.getName());

    private List<String> nodes;

    public Session setUp() {
        logger.info("Requesting list of nodes");
        return MessageBuilder.buildSessionMessage(SessionInfo.getInstance().getID());
    }

    public boolean handleResponse(EncryptedMessage response) {
        try {
            NodeList nodeList = NodeList.parseFrom(decryptMessage(response));
            logger.info("Received list of nodes of length " +
                    nodeList.getNodesList().size() + ".");
            nodes = toStringList(nodeList.getNodesList());
            return true;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> getNodes() {
        return nodes;
    }
}
