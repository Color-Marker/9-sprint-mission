package services.workTest;

import entity.Channel;
import entity.ChannelType;
import entity.Message;
import entity.User;
import services.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkChannelService implements ChannelService {
    private final List<Channel> data;
    private final WorkMessageService jcfMessageService;

    public WorkChannelService(WorkMessageService jcfMessageService){
        data = new ArrayList<>() {};
        this.jcfMessageService = jcfMessageService;
    }

    @Override
    public boolean addChannel(Channel channel) {
        return data.add(channel);
    }

    @Override
    public Channel getChannelById(UUID id) {
        for(Channel c : data){
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Channel> getChannelByName(String channelName){
        List<Channel> buffer = new ArrayList<>();
        for(Channel c: data){
            if(c.getChannelName().equals(channelName)){
                buffer.add(c);
            }
        }
        return buffer;
    }

    @Override
    public List<Channel> getAllChannel() {
        return data;
    }

    @Override
    public boolean updateChannelName(Channel channel, String channelName) {
        for(Channel c:data){
            if(c.equals(channel)){
                c.setChannelName(channelName);
                c.setUpdatedAt();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeChannelType(Channel channel, ChannelType channelType) {
        for(Channel c: data){
            if(c.equals(channel)){
                c.setChanneltype(channelType);
                c.setUpdatedAt();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean sendMessageToChannel(Channel channel, Message message) {
        for(Channel c: data){
            if(c.equals(channel)){
                c.getMessages().add(message);
                return jcfMessageService.addMessage(message);
            }
        }
        return false;
    }

    @Override
    public List<Message> getAllMessageFromThatUser(Channel channel, User user) {
        List<Message> buffer = new ArrayList<>();
        for(Channel c: data){
            if(c.equals(channel)){
                for(Message m : c.getMessages()){
                    if(m.getSender().equals(user)){
                        buffer.add(m);
                    }
                }
            }
        }
        return buffer;
    }

    @Override
    public boolean deleteChannel(Channel channel) {
        for(Channel c:data){
            if(c.equals(channel)){
                data.remove(c);
                return true;
            }
        }
        return false;
    }
}
