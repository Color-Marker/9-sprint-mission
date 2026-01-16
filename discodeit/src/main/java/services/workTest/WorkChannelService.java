package services.workTest;

import entity.*;
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
    public void addChannel(Channel channel) {
        data.add(channel);
    }

    @Override
    public void getChannelById(UUID id) {
        data.stream()
                .filter(c -> c.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Channel> getChannelByName(String channelName){
        return data.stream()
                .filter(c->c.getChannelName().equals(channelName))
                .toList();
    }

    @Override
    public void getAllChannel() {
    }

    @Override
    public List<Channel> getAllChannelByServer(ServerRoom server) {
        return data.stream()
                .filter(c->c.getServerRoom().equals(server))
                .toList();
    }

    @Override
    public void updateChannelName(Channel channel, String channelName) {
        data.stream()
                .filter(c -> c.equals(channel))
                .findAny()
                .map(c -> c.setChannelName(channelName));
    }

    @Override
    public void changeChannelType(Channel channel, ChannelType channelType) {
        data.stream()
                .filter(c -> c.equals(channel))
                .findAny()
                .map(c -> c.setChannelType(channelType));
    }

    @Override
    public void sendMessageToChannel(Channel channel, Message message) {
        data.stream()
                .filter(c -> c.equals(channel))
                .findAny()
                .map(c -> {
                    c.getMessages().add(message);
                    return messageService.addMessage(message);
                });
    }

    @Override
    public void getAllMessageFromThatUser(Channel channel, User user) {
        data.stream()
                .filter(c -> c.equals(channel))
                .flatMap(c -> c.getMessages().stream())
                .filter(m -> m.getSender().equals(user))
                .toList();
    }

    @Override
    public List<Message> getAllMessage(Channel channel) {
        return messageService.getMessageByChannel(channel);
    }


    @Override
    public void deleteChannel(Channel channel) {
        for(Message m: channel.getMessages()){
            messageService.deleteMessage(m);
        }
        data.removeIf(c -> c.equals(channel));
    }
}
