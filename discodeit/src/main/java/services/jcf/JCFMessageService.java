package services.jcf;

import entity.Message;
import entity.User;
import services.MessageService;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageService implements MessageService {
    private final List<Message> data;

    public JCFMessageService(){
        data = new ArrayList<>() {};
    }

    @Override
    public boolean addMessage(Message message) {
        data.add(message);
        System.out.println("New message: " + message.getSender().getDisplayName() + " sent " + message);
        return true;
    }

    @Override
    public List<Message> getMessageFromThatUser(User user) {
        List<Message> buffer = new ArrayList<>();
        for(Message m : data){
            if(m.getSender().equals(user)){
                buffer.add(m);
            }
        }
        if(!buffer.isEmpty()){
            return buffer;
        }
        else{
            return null;
        }
    }

    @Override
    public List<Message> getAllMessage() {
        return data;
    }

    @Override
    public Message updateMessage(Message message, String messageContent) {
        for(Message m : data){
            if(m.equals(message)){
                m.setUpdatedAt( System.currentTimeMillis());
                m.setMessageContent(messageContent);
                System.out.println("Message Edited at " + m.getUpdatedAt() + " to " + m.getMessageContent());
                return m;
            }
        }
        return null;
    }

    @Override
    public boolean deleteMessage(Message message) {
        for(Message m : data){
            if(m.equals(message)){
                System.out.println("Message deleted: " + m);
                data.remove(m);
                return true;
            }
        }
        return false;
    }
}
