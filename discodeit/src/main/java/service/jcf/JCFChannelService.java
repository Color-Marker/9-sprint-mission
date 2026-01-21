package service.jcf;

import entity.*;
import service.ChannelService;
import service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final List<Channel> data;
    private final MessageService messageService;

    public JCFChannelService(MessageService messageService){
        data = new ArrayList<>() {};
        this.messageService = messageService;
    }

    @Override
    public void addChannel(Channel channel) {
        System.out.println("-- Channel add complete: " + channel.getChannelName() + " --");
        data.add(channel);
    }

    @Override
    public Channel getChannelById(UUID id) {
        for(Channel c : data){
            if(c.getId().equals(id)){
                System.out.println("-- Find channel by id: " + id + " --");
                System.out.println("name: " + c.getChannelName());
                System.out.println("channel type: " + c.getChanneltype());
                System.out.println();
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Channel> getChannelByName(String channelName){
        List<Channel> buffer = new ArrayList<>();
        System.out.println("-- Find channel by name: " + channelName + " --");
        for(Channel c: data){
            if(c.getChannelName().equals(channelName)){
                System.out.println("id: " + c.getId());
                System.out.println("channel type: " + c.getChanneltype());
                buffer.add(c);
            }
        }
        System.out.println();
        return buffer;
    }

    @Override
    public List<Channel> getAllChannel() {
        System.out.println("-- All channel information --");
        for(Channel c: data){
            System.out.println("id: " + c.getId());
            System.out.println("name: " + c.getChannelName());
            System.out.println("type: " + c.getChanneltype());
            System.out.println();
        }
        return new ArrayList<>(data);
    }

    @Override
    public List<Channel> getAllChannelByServer(ServerRoom server) {
        return data.stream()
                .filter(c->c.getServerRoom().equals(server))
                .toList();
    }
    @Override
    public void updateChannelName(Channel channel, String channelName) {
        for(Channel c:data){
            if(c.equals(channel)){
                System.out.println("-- Updated channel name --");
                System.out.println("Channel " + c.getChannelName() + " changed name");
                System.out.print(c.getChannelName() + " -> ");
                c.setChannelName(channelName);
                System.out.println(c.getChannelName());
                System.out.println();
                return;
            }
        }
    }

    @Override
    public void changeChannelType(Channel channel, ChannelType channelType) {
        for(Channel c: data){
            if(c.equals(channel)){
                System.out.println("-- Updated channel type --");
                System.out.println("Channel " + c.getChannelName() + " changed type");
                System.out.print(c.getChanneltype() + " -> ");
                c.setChannelType(channelType);
                System.out.println(c.getChanneltype());
                System.out.println();
                return;
            }
        }
    }

    @Override
    public void sendMessageToChannel(Channel channel, Message message) {
        for(Channel c: data){
            if(c.equals(channel)){
                System.out.println("-- Send message in channel " + c.getChannelName() + " --");
                c.getMessages().add(message);
                System.out.println("sender: " + message.getSender().getDisplayName());
                System.out.println("new message: " + message.getMessageContent());
                System.out.println();
                messageService.addMessage(message);
                return;
            }
        }
    }

    @Override
    public List<Message> getAllMessageFromThatUser(Channel channel, User user) {
        List<Message> buffer = new ArrayList<>();
        for(Channel c: data){
            if(c.equals(channel)){
                System.out.println("-- All messages from " + user.getDisplayName() + " in channel " + c.getChannelName() + " --");
                for(Message m : c.getMessages()){
                    if(m.getSender().equals(user)){
                        System.out.println("'" + m.getMessageContent() + "'");
                        buffer.add(m);
                    }
                }
            }
        }
        System.out.println();
        return buffer;
    }

    @Override
    public void deleteChannel(Channel channel) {
        for(Channel c:data){
            if(c.equals(channel)){
                System.out.println("-- Deleted channel " + c.getChannelName() + " --");
                data.remove(c);
                System.out.println();
                return;
            }
        }
    }

    @Override
    public void updateMessageInChannel(Channel channel, Message message, String newContent) {
        return;
    }

    @Override
    public List<Message> getAllMessage(Channel channel) {
        return messageService.getMessageByChannel(channel);
    }

}
