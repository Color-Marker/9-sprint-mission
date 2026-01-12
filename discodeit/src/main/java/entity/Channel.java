package entity;

import java.util.List;
import java.util.UUID;

public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private List<Message> messages;
    private ChannelType channeltype;
    private String channelName;

    public Channel(ChannelType channelType, String channelName) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.messages = null;
        this.channeltype = channelType;
        this.channelName = channelName;
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

    public List<Message> getMessages() {
        return messages;
    }

    public ChannelType getChanneltype() {
        return channeltype;
    }

    public String getChannelName() {
        return channelName;
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

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setChanneltype(ChannelType channeltype) {
        this.channeltype = channeltype;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
