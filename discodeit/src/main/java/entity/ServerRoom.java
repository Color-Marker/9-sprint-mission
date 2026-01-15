package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerRoom {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private List<Channel> channel;
    private User owner;
    private List<User> member;
    private String serverName;

    public ServerRoom(User owner, String serverName) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.channel = new ArrayList<>();
        this.owner = owner;
        this.member = new ArrayList<>();
        this.serverName = serverName;
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

    public List<Channel> getChannel() {
        return channel;
    }

    public User getOwner() {
        return owner;
    }

    public List<User> getMember() {
        return member;
    }

    public String getServerName() {
        return serverName;
    }

    private void setUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }

    public boolean setChannel(List<Channel> channel) {
        if (channel == null || channel.isEmpty()) {
            return false;
        }
        setUpdatedAt();
        this.channel = channel;
        return true;
    }

    public boolean setOwner(User owner) {
        if (owner == null) {
            return false;
        }
        setUpdatedAt();
        this.owner = owner;
        return true;
    }

    public boolean setMember(List<User> member) {
        if (member == null) {
            return false;
        }
        setUpdatedAt();
        this.member = member;
        return true;
    }

    public boolean setServerName(String serverName) {
        if (serverName == null) {
            return false;
        }
        setUpdatedAt();
        this.serverName = serverName;
        return true;
    }
}
