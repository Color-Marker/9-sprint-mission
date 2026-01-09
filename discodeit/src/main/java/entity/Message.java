package entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private String messageContent;
    private User sender;

    public Message(String messageContent, User sender) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.messageContent = messageContent;
        this.sender = sender;
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


    public String getMessageContent() {
        return messageContent;
    }

    public User getSender() {
        return sender;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }


    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
