package services;

import entity.Message;
import entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    // 현재 구조 상 채널 별로 메시지 리스트가 있는 구조.
    boolean addMessage(Message message);    // 메시지 추가

    // 특정 유저의 메시지만 가져오기 - user 쪽에서 이름이나 id로 찾아서 유저 받아오고, 그거 이용해서 메시지 가져오기
    List<Message> getMessageFromUser(User user);
    // 모든 유저 메시지 받기
    List<Message> getAllMessage();
    // 특정 String 내용 가진 메시지만 가져오기
    List<Message> getMessageByContent(String messageContent);
    // 메시지 수정 - 대상 메시지는 get 방식들로 가져와서 써먹기
    boolean updateMessage(Message message, String messageContent);

    // 특정 메시지 삭제
    boolean deleteMessage(Message message);
}