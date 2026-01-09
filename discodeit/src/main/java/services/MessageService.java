package services;

import entity.Message;
import entity.User;

import java.util.List;

public interface MessageService {
    boolean addMessage(Message message);

    // 특정 유저의 메시지만 받
    List<Message> getMessageFromThatUser(User user);
    // 모든 유저 메시지 받기
    List<Message> getAllMessage();

    Message updateMessage(Message message, String messageContent);

    boolean deleteMessage(Message message);


}
