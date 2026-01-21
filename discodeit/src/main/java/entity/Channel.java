package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private List<Message> messages;
    private ChannelType channeltype;
    private String channelName;
    private final ServerRoom serverRoom;

    public Channel(ChannelType channelType, String channelName, ServerRoom serverRoom) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.messages = new ArrayList<>();
        this.channeltype = channelType;
        this.channelName = channelName;
        this.serverRoom = serverRoom;
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

    public ServerRoom getServerRoom() {
        return serverRoom;
    }

    private void setUpdatedAt() {
        this.updatedAt =System.currentTimeMillis();
    }

    public boolean setMessages(List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return false;
        }
        setUpdatedAt();
        this.messages = messages;
        return true;
    }

    public boolean setChannelType(ChannelType channeltype) {
        if (channeltype == null) {
            return false;
        }
        setUpdatedAt();
        this.channeltype = channeltype;
        return true;
    }

    public boolean setChannelName(String channelName) {
        if (channelName == null) {
            return false;
        }
        setUpdatedAt();
        this.channelName = channelName;
        return true;
    }
}
