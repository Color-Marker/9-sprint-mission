package services.workTest;

import entity.Channel;
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
    public void getMessageById(UUID id) {
        data.stream()
                .filter(m -> m.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Message> getMessageFromUser(User user) {
        return data.stream()
                .filter(m->m.getSender().equals(user))
                .toList();
    }

    @Override
    public void getAllMessage() {
    }

    @Override
    public void getMessageByContent(String messageContent) {
        data.stream()
                .filter(m -> m.getMessageContent().contains(messageContent))
                .toList();
    }

    @Override
    public List<Message> getMessageByChannel(Channel channel) {
        return data.stream()
                .filter(m->m.getChannel().equals(channel))
                .toList();
    }


    @Override
    public void updateMessage(Message message, String messageContent) {
        data.stream()
                .filter(m -> m.equals(message))
                .findAny()
                .map(m -> m.setMessageContent(messageContent));
    }

    @Override
    public void deleteMessage(Message message) {
        data.removeIf(m -> m.equals(message));
    }

}
