package services.workTest;

import entity.Channel;
import entity.ChannelType;
import entity.Message;
import entity.User;
import services.ChannelService;
import services.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkChannelService implements ChannelService {
    private final List<Channel> data;
    private final MessageService messageService;

    public WorkChannelService(MessageService messageService){
        data = new ArrayList<>() {};
        this.messageService = messageService;
    }

    @Override
    public boolean addChannel(Channel channel) {
        return data.add(channel);
    }

    @Override
    public Channel getChannelById(UUID id) {
        return data.stream()
                .filter(c->c.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<Channel> getChannelByName(String channelName){
        return data.stream()
                .filter(c->c.getChannelName().equals(channelName))
                .toList();
    }

    @Override
    public List<Channel> getAllChannel() {
        return new ArrayList<>(data);
    }

    @Override
    public boolean updateChannelName(Channel channel, String channelName) {
        return data.stream()
                .filter(c->c.equals(channel))
                .findAny()
                .map(c-> c.setChannelName(channelName))
                .orElse(false);
    }

    @Override
    public boolean changeChannelType(Channel channel, ChannelType channelType) {
        return data.stream()
                .filter(c->c.equals(channel))
                .findAny()
                .map(c-> c.setChannelType(channelType))
                .orElse(false);
    }

    @Override
    public boolean sendMessageToChannel(Channel channel, Message message) {
        return data.stream()
                .filter(c->c.equals(channel))
                .findAny()
                .map(c->{
                    c.getMessages().add(message);
                    return messageService.addMessage(message);
                })
                .orElse(false);
    }

    @Override
    public List<Message> getAllMessageFromThatUser(Channel channel, User user) {
        return data.stream()
                .filter(c->c.equals(channel))
                .flatMap(c->c.getMessages().stream())
                .filter(m->m.getSender().equals(user))
                .toList();
    }

    @Override
    public boolean deleteChannel(Channel channel) {
        return data.removeIf(c->c.equals(channel));
    }
}
