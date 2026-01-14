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
        System.out.println("-- Message sent by " + message.getSender().getDisplayName() + " --");
        System.out.println("'" + message.getMessageContent() + "'");
        System.out.println();
        return data.add(message);
    }

    @Override
    public Message getMessageById(UUID id) {
        for(Message m: data){
            if(m.getId().equals(id)){
                System.out.println("-- Searching message by id: " + id + " --");
                System.out.println("sender: " + m.getSender().getDisplayName());
                System.out.println("'" + m.getMessageContent() + "'");
                System.out.println();
                return m;
            }
        }
        return null;
    }

    @Override
    public List<Message> getMessageFromUser(User user) {
        List<Message> buffer = new ArrayList<>();
        System.out.println("-- All Message that " + user.getDisplayName() + " sent --");
        for(Message m : data){
            if(m.getSender().equals(user)){
                System.out.println("'" + m.getMessageContent() + "'");
                buffer.add(m);
            }
        }
        System.out.println();
        return buffer;
    }

    @Override
    public List<Message> getAllMessage() {
        System.out.println(" -- All message information --");
        for(Message m: data){
            System.out.println("id: " + m.getId());
            System.out.println("sender: " + m.getSender().getDisplayName());
            System.out.println("'" + m.getMessageContent() + "'");
            System.out.println();
        }
        return data;
    }

    @Override
    public List<Message> getMessageByContent(String messageContent) {
        List<Message> buffer = new ArrayList<>();
        System.out.println("-- Searching message by content: " + messageContent + " --");
        for(Message m : data){
            if(m.getMessageContent().contains(messageContent)){
                System.out.println("'" + m.getMessageContent() + "' from " + m.getSender().getDisplayName());
                buffer.add(m);
            }
        }
        System.out.println();
        return buffer;
    }


    @Override
    public boolean updateMessage(Message message, String messageContent) {
        for(Message m: data){
            if(m.equals(message)){
                System.out.println("-- Update message --");
                System.out.println("id: " + m.getId());
                System.out.println("sender: " + m.getSender());
                System.out.print("'" + m.getMessageContent() + "' -> '");
                m.setMessageContent(messageContent);
                System.out.println(m.getMessageContent() +"'");
                System.out.println();
                m.setUpdatedAt();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteMessage(Message message) {
        for(Message m: data){
            if(m.equals(message)){
                System.out.println("-- Deleted message '" + m.getMessageContent() + "' by user " + m.getSender().getDisplayName() + " --");
                data.remove(m);
                System.out.println();
                return true;
            }
        }
        return false;
    }

}
