package service.basic;

import entity.*;
import repository.ChannelRepository;
import service.ChannelService;
import service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageService messageService;
    public BasicChannelService(ChannelRepository channelRepository, MessageService messageService) {
        this.channelRepository = channelRepository;
        this.messageService = messageService;
    }

    @Override
    public void addChannel(Channel channel) {
        channelRepository.save(channel);
    }

    @Override
    public Channel getChannelById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> getChannelByName(String name) {
        return channelRepository.findAll().stream()
                .filter(c -> c.getChannelName().equals(name))
                .toList();
    }

    @Override
    public List<Channel> getAllChannel() {
        return channelRepository.findAll();
    }

    @Override
    public List<Channel> getAllChannelByServer(ServerRoom server) {
        return channelRepository.findAll().stream()
                .filter(c->c.getServerRoom().getId().equals(server.getId()))
                .toList();
    }

    @Override
    public void updateChannelName(Channel channel, String channelName) {
        Channel target = channelRepository.findById(channel.getId());
        if(target == null){
            throw new NoSuchElementException("Can't find target");
        }
        target.setChannelName(channelName);
        channelRepository.save(target);
    }

    @Override
    public void changeChannelType(Channel channel, ChannelType channelType) {
        Channel target = channelRepository.findById(channel.getId());
        if(target == null){
            throw new NoSuchElementException("Can't find target");
        }
        target.setChannelType(channelType);
        channelRepository.save(target);
    }

    @Override
    public void sendMessageToChannel(Channel channel, Message message) {
        channelRepository.findById(channel.getId())
                .getMessages().add(message);
        messageService.addMessage(message);
    }

    @Override
    public List<Message> getAllMessageFromThatUser(Channel channel, User user) {
        List<Message> channelMsg = messageService.getMessageByChannel(channel);
        return channelMsg.stream()
                .filter(m->m.getSender().getId().equals(user.getId()))
                .toList();
    }

    @Override
    public List<Message> getAllMessage(Channel channel) {
        return messageService.getMessageByChannel(channel);
    }

    @Override
    public void deleteChannel(Channel channel) {
        List<Message> target = channelRepository.findById(channel.getId()).getMessages();
        for(Message m : target){
            messageService.deleteMessage(m);
        }
        channelRepository.deleteById(channel.getId());
    }

    @Override
    public void updateMessageInChannel(Channel channel, Message message, String newContent) {
        Channel where = channelRepository.findById(channel.getId());
        Message target = where.getMessages().stream().filter(m -> m.getId().equals(message.getId()))
                .findAny().orElse(null);
        if(target == null){
            return;
        }
        target.setMessageContent(newContent);
        messageService.updateMessage(message, newContent);
    }
}
