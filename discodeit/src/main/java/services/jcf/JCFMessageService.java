package services.jcf;

import entity.Message;
import entity.User;
import services.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final List<Message> data;

    public JCFMessageService(){
        data = new ArrayList<>() {};
    }


    @Override
    public boolean addMessage(Message message) {
        return data.add(message);
    }

    @Override
    public List<Message> getMessageFromUser(User user) {
        List<Message> buffer = new ArrayList<>();
        for(Message m : data){
            if(m.getSender().equals(user)){
                buffer.add(m);
            }
        }
        return buffer;
    }

    @Override
    public List<Message> getAllMessage() {
        return data;
    }

    @Override
    public List<Message> getMessageByContent(String messageContent) {
        List<Message> buffer = new ArrayList<>();
        for(Message m : data){
            if(m.getMessageContent().contains(messageContent)){
                buffer.add(m);
            }
        }
        return buffer;
    }

    @Override
    public Message getMessageById(UUID id) {
        for(Message m : data){
            if(m.getId().equals(id)){
                return m;
            }
        }
        return null;
    }

    @Override
    public boolean updateMessage(Message message, String messageContent) {
        for(Message m: data){
            if(m.equals(message)){
                m.setMessageContent(messageContent);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteMessage(Message message) {
        for(Message m: data){
            if(m.equals(message)){
                data.remove(m);
                return true;
            }
        }
        return false;
    }

}
