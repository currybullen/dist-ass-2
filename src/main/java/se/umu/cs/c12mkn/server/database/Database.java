package se.umu.cs.c12mkn.server.database;

import se.umu.cs.c12mkn.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by c12mkn on 11/6/15.
 */
public class Database {
    private static final Database instance = new Database();

    private Map<String, Message> messagesByID;
    private Map<String, List<String>> messagesByTopic;
    private Map<String, List<String>> subscribers;
    private List<String> nodes;

    private Database() {
        messagesByID = new HashMap();
        messagesByTopic = new HashMap();
        subscribers = new HashMap();
        nodes = new ArrayList();
    }

    /**
     * Returns the sole instance of this database.
     * @return the sole instance of this database.
     */
    public static Database getInstance() {
        return instance;
    }

    /**
     * Adds a message.
     * @param message the message to be added.
     */
    public synchronized void addMessage(Message message) {
        storeMessage(message);
    }

    /**
     * Returns the message with a specific id.
     * @param id the id of the message.
     * @return the message with the specific id.
     */
    public synchronized Message getMessage(String id) {
        return messagesByID.get(id);
    }

    /**
     * Returns a list of all messages of a specific topic.
     * @return a list of all messages of a specific topic.
     */
    public synchronized List<String> getMessagesByTopic(String topic) {
        return messagesByTopic.get(topic);
    }

    /**
     * Returns a list of all topics.
     * @return a list of all topics.
     */
    public synchronized List<String> getTopics() {
        return new ArrayList(messagesByTopic.keySet());
    }

    /**
     * Adds a subscriber to a topic, creating the topic if it doesn't exist.
     * @param topic the topic to subscribe to.
     * @param subscriber the subscriber.
     */
    public synchronized boolean addSubscriber(String topic, String subscriber) {
        if (!subscribers.containsKey(topic))
            subscribers.put(topic, new ArrayList<String>());
        subscribers.get(topic).add(subscriber);
        return true;
    }

    /**
     * Removes a subscriber from a topic, if the topic exists.
     * @param topic the topic subscribed to.
     * @param subscriber the subscriber.
     */
    public synchronized boolean removeSubscriber(String topic, String subscriber) {
        if (subscribers.get(topic) != null)
            subscribers.get(topic).remove(subscriber);
        return true;
    }

    /**
     * Returns a list of all subscribers for a topic.
     * @param topic the topic subscribed to.
     * @return a list of all subscribers for a topic.
     */
    public synchronized List<String> getSubscribers(String topic) {
        return subscribers.get(topic);
    }

    public synchronized void addNode(String node) {
        nodes.add(node);
    }

    public synchronized List<String> getNodes() {
        return nodes;
    }

    private void storeMessage(Message message) {
        messagesByID.put(message.getId(), message);
        if (!messagesByTopic.containsKey(message.getTopic()))
            messagesByTopic.put(message.getTopic(), new ArrayList<String>());
        messagesByTopic.get(message.getTopic()).add(message.getId());
    }
}
