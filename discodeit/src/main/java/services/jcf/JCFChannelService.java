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
        System.out.println("Channel add complete: " + channel.getChannelName());
        return data.add(channel);
    }

    @Override
    public List<Channel> getChannelByName(String channelName){
        List<Channel> buffer = new ArrayList<>();
        for(Channel c: data){
            if(c.getChannelName().equals(channelName)){
                System.out.println("Find channel by name: " + c.getChannelName());
                System.out.println("Extra information");
                System.out.println("id: " + c.getId() + " channel type: " + c.getChanneltype());
                buffer.add(c);
            }
        }
        return buffer;
    }

    @Override
    public List<Channel> getAllChannel() {
        System.out.println("All channel information: ");
        for(Channel c: data){
            System.out.println("id: " + c.getId() + " name: " + c.getChannelName());
            System.out.println("type: " + c.getChanneltype());
        }
        return data;
    }

    @Override
    public boolean updateChannelName(Channel channel, String channelName) {
        for(Channel c:data){
            if(c.equals(channel)){
                c.setChannelName(channelName);
                System.out.println("Updated channel name");
                System.out.println("Channel id " + c.getId() + " changed name to " + c.getChannelName());
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
                System.out.println("Updated channel type");
                System.out.println("Channel id " + c.getId() + " which name is " + c.getChannelName() +  " changed type to " + c.getChanneltype());
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
                System.out.println("In " + c.getChannelName() + " new message '" + message.getMessageContent() + "' added");
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
                System.out.println("In channel " + c.getChannelName() + " user " + user.getDisplayName() + " sent ");
                for(Message m : c.getMessages()){
                    if(m.getSender().equals(user)){
                        System.out.println(m.getMessageContent());
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
                System.out.println("Deleted channel " + c.getChannelName());
                data.remove(c);
                return true;
            }
        }
        return false;
    }
}
