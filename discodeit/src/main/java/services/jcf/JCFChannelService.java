package services.jcf;

import entity.Channel;
import entity.ChannelType;
import entity.Message;
import entity.User;
import services.ChannelService;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService {
    private final List<Channel> data;

    public JCFChannelService(){
        data = new ArrayList<>() {};
    }

    @Override
    public boolean addChannel(Channel channel) {
        return data.add(channel);
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
                return true;
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
    public boolean updateChannel(Channel channel, String channelName) {
        for(Channel c : data){
            if(c.equals(channel)){
                c.setChannelName(channelName);
                return true;
            }
        }
        return false;
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
