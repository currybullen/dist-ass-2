package se.umu.cs.c12mkn;

import com.google.common.io.ByteStreams;
import se.umu.cs.c12mkn.client.MessageClient;
import se.umu.cs.c12mkn.message.Message;
import se.umu.cs.c12mkn.server.MessageServer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;

/**
 * Created by c12mkn on 11/17/16.
 */
public class ServerClientTester {
    private final int port;
    private final String algorithm;
    private final byte[] publicKey;
    private final byte[] privateKey;


    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Not enough arguments");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String algorithm = args[1];
        LogManager.getLogManager().reset();
        new ServerClientTester(port, algorithm).run();
    }

    public ServerClientTester(int port, String algorithm) {
        this.port = port;
        this.algorithm = algorithm;
        publicKey = loadKey("/serverkeys/pubkey");
        privateKey = loadKey("/serverkeys/privkey");
    }

    public void run() {
        new Thread() {
            @Override
            public void run() {
                try {
                    MessageServer messageServer = new MessageServer(port, publicKey, privateKey);
                    messageServer.start();
                    messageServer.blockUntilShutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        runClientTests();
    }

    public void runClientTests() {
        MessageClient messageClient =
                new MessageClient("localhost", port, publicKey);

        if (algorithm.equals("RSA"))
            messageClient.rsaKeyExchange();
        if (algorithm.equals("AES"))
            messageClient.performDHKeyExchange();
        if (algorithm.equals("custom"))
            messageClient.setUpCustomEncryption();

        String challenge = messageClient.initAuth("currybullen");
        System.out.println("User 'currybullen' receives challenge '" +
                challenge + "', responds with answer 'ZfDPxY5Y'\n");
        messageClient.authenticate("currybullen", "nkSW4rs5", "ZfDPxY5Y");

        se.umu.cs.c12mkn.message.Message messageA =
                new Message("1", System.currentTimeMillis(), "Micke", "Anna",
                        "Viktigt","<3","kramkalas".getBytes());
        se.umu.cs.c12mkn.message.Message messageB =
                new Message("2", System.currentTimeMillis(), "Anna", "Micke",
                        "Viktigt", "critipiii", "bullkalas".getBytes());

        System.out.println("Storing two messages with topic 'Viktigt' on server.\n");
        messageClient.postMessage(messageA);
        messageClient.postMessage(messageB);

        System.out.println("Fetching a list of messages for topic 'Viktigt'.");
        List<String> messages = messageClient.listMessages("Viktigt");
        System.out.println("Messages with topic 'Viktigt': " + messages.toString() + "\n");

        System.out.println("Fetching timestamps.");
        Map<String, Long> timestamps = messageClient.listMessagesWithTimestamps("Viktigt");
        System.out.println("Timestamps of messages with topic 'Viktigt': " + timestamps.toString() + "\n");

        System.out.println("Retrieving a message:");
        Message message = messageClient.retrieveMessage("2");
        System.out.println("Message id: " + message.getId() +
                "\nTimestamp: " + message.getTimestamp() +
                "\nSender: " + message.getSender() +
                "\nRecipient: " + message.getRecipient() +
                "\nTopic: " + message.getTopic() +
                "\nContent: " + message.getContent() +
                "\nAttachments: " + message.getAttachments() + "\n");

        System.out.println("Retrieving a list of topics.");
        List<String> topics = messageClient.listTopics();
        System.out.println("List of topics: " + topics.toString() + "\n");

        System.out.println("Subscribing user 'currybullen' to 'Viktigt'.");
        messageClient.subscribe("currybullen", "Viktigt");
        System.out.println("Subscribing user 'paprikafix' to 'Viktigt'.");
        messageClient.subscribe("paprikafix", "Viktigt");
        System.out.println("Unsubscribing user 'currybullen' from 'Viktigt'.\n");
        messageClient.unsubscribe("currybullen", "Viktigt");

        System.out.println("Retrieving a list of 'Viktigt' subscribers.");
        List<String> subscribers = messageClient.listSubscribers("Viktigt");
        System.out.println("List of subscribers: " + subscribers.toString() + "\n");

        System.out.println("Retrieving a list of nodes.");
        List<String> nodes = messageClient.listNodes();
        System.out.println("List of nodes: " + nodes.toString());
        System.exit(0);
    }

    private byte[] loadKey(String path) {
        try {
            return ByteStreams.toByteArray(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
