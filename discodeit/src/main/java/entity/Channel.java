package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private List<Message> messages;
    private ChannelType channeltype;
    private String channelName;

    public Channel(ChannelType channelType, String channelName) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.messages = new ArrayList<>();
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

    public void setUpdatedAt() {
        this.updatedAt =System.currentTimeMillis();
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
