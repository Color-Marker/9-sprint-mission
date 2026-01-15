package services.workTest;

import entity.Message;
import entity.User;
import services.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkMessageService implements MessageService {
    private final List<Message> data;

    public WorkMessageService(){
        data = new ArrayList<>() {};
    }


    @Override
    public boolean addMessage(Message message) {
        return data.add(message);
    }

    @Override
    public Message getMessageById(UUID id) {
        return data.stream()
                .filter(m->m.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<Message> getMessageFromUser(User user) {
        return data.stream()
                .filter(m->m.getSender().equals(user))
                .toList();
    }

    @Override
    public List<Message> getAllMessage() {
        return new ArrayList<>(data);
    }

    @Override
    public List<Message> getMessageByContent(String messageContent) {
        return data.stream()
                .filter(m->m.getMessageContent().contains(messageContent))
                .toList();
    }


    @Override
    public boolean updateMessage(Message message, String messageContent) {
        return data.stream()
                .filter(m->m.equals(message))
                .findAny()
                .map(m-> m.setMessageContent(messageContent))
                .orElse(false);
    }

    @Override
    public boolean deleteMessage(Message message) {
        return data.removeIf(m->m.equals(message));
    }

}
