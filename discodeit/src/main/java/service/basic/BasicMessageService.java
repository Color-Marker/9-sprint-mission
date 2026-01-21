package service.basic;

import entity.Channel;
import entity.Message;
import entity.User;
import repository.MessageRepository;
import service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public boolean addMessage(Message message) {
        return messageRepository.save(message) != null;
    }

    @Override
    public Message getMessageById(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> getMessageFromUser(User user) {
        return messageRepository.findAll().stream()
                .filter(m->m.getSender().getId().equals(user.getId()))
                .toList();
    }

    @Override
    public List<Message> getAllMessage() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> getMessageByContent(String messageContent) {
        return messageRepository.findAll().stream()
                .filter(m->m.getMessageContent().contains(messageContent))
                .toList();
    }

    @Override
    public List<Message> getMessageByChannel(Channel channel) {
        return messageRepository.findAll().stream()
                .filter(m->m.getChannel().getId().equals(channel.getId()))
                .toList();
    }

    @Override
    public void updateMessage(Message message, String messageContent) {
        Message target = messageRepository.findById(message.getId());
        if(target == null){
            throw new NoSuchElementException("Can't find target");
        }
        target.setMessageContent(messageContent);
        messageRepository.save(target);
    }

    @Override
    public void deleteMessage(Message message) {
        messageRepository.deleteById(message.getId());
    }
}
