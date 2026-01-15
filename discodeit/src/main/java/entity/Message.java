package entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String messageContent;
    private User sender;
    private final Channel channel;

    public Message(String messageContent, User sender, Channel channel) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.messageContent = messageContent;
        this.sender = sender;
        this.channel = channel;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public User getSender() {
        return sender;
    }


    private void setUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();;
    }


    public boolean setMessageContent(String messageContent) {
        if (messageContent == null) {
            return false;
        }
        setUpdatedAt();
        this.messageContent = messageContent;
        return true;
    }

    public boolean setSender(User sender) {
        if (sender == null) {
            return false;
        }
        setUpdatedAt();
        this.sender = sender;
        return true;
    }

}
