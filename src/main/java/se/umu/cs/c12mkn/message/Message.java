package se.umu.cs.c12mkn.message;

/**
 * Created by currybullen on 11/12/16.
 */
public class Message {
    private final String id;
    private final long timestamp;;
    private final String sender;
    private final String recipient;
    private final String topic;
    private final String content;
    private final byte[] attachments;

    public Message(String id,
                   long timestamp,
                   String sender,
                   String recipient,
                   String topic,
                   String content,
                   byte[] attachments) {
        this.id = id;
        this.timestamp = timestamp;
        this.sender = sender;
        this.recipient = recipient;
        this.topic = topic;
        this.content = content;
        this.attachments = attachments;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getTopic() {
        return topic;
    }

    public String getContent() {
        return content;
    }

    public byte[] getAttachments() {
        return attachments;
    }
}
