package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerRoom {
    private UUID id;
    private Long createdAt;
    private List<Channel> channel;
    private User owner;
    private List<User> member;
    private String serverName;

    public ServerRoom(User owner, String serverName) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setChannel(List<Channel> channel) {
        this.channel = channel;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setMember(List<User> member) {
        this.member = member;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
