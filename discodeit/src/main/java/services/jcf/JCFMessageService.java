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
        System.out.println("Message add complete: " + message.getMessageContent());
        return data.add(message);
    }

    @Override
    public List<Message> getMessageFromUser(User user) {
        List<Message> buffer = new ArrayList<>();
        System.out.println("User " + user.getDisplayName() + " sent ");
        for(Message m : data){
            if(m.getSender().equals(user)){
                System.out.println("'" + m.getMessageContent() + "'");
                buffer.add(m);
            }
        }
        return buffer;
    }

    @Override
    public List<Message> getAllMessage() {
        System.out.println("All message sent: ");
        for(Message m: data){
            System.out.println("id: " + m.getId() + " sender: " + m.getSender().getDisplayName());
            System.out.println("'" + m.getMessageContent() + "'");
        }
        return data;
    }

    @Override
    public List<Message> getMessageByContent(String messageContent) {
        List<Message> buffer = new ArrayList<>();
        System.out.println("Searching message...");
        for(Message m : data){
            if(m.getMessageContent().contains(messageContent)){
                System.out.println("'" + m.getMessageContent() + "' from " + m.getSender().getDisplayName());
                buffer.add(m);
            }
        }
        return buffer;
    }


    @Override
    public boolean updateMessage(Message message, String messageContent) {
        for(Message m: data){
            if(m.equals(message)){
                m.setMessageContent(messageContent);
                System.out.println("Update message " + m.getId()+ " content to '" + m.getMessageContent() +"'");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteMessage(Message message) {
        for(Message m: data){
            if(m.equals(message)){
                System.out.println("Deleted message '" + m.getMessageContent() + "'");
                data.remove(m);
                return true;
            }
        }
        return false;
    }

}
