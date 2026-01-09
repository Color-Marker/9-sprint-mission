import entity.Message;
import entity.User;
import services.jcf.JCFMessageService;
import services.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {

        // --- user ---
        User user1 = new User("abc", "abc@codeit.com", "01099999999");
        User user2 = new User("asdf", "asdf@codeit.com", "01000000000");
        User user3 = new User("abcf", "abcf@codeit.com", "01055555555");
        JCFUserService userService = new JCFUserService();

        // 등록
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);

        System.out.println(); // 구분용

        // 조회 (한 건)
        System.out.println(userService.getUser("abc"));
        System.out.println(userService.getUser("asdf"));
        System.out.println(userService.getUser("abcf"));

        System.out.println();

        // 여러 개 조회 (다건)
        System.out.println(userService.getUsers("abc", "asdf"));

        System.out.println();

        // 전체 조회 (다건)
        System.out.println(userService.getAllUser());

        System.out.println();

        // 수정 데이터 조회
        System.out.println(userService.updateUser(user3, "alice", "alice@codeit.com", "01088888888"));

        System.out.println();

        // 삭제
        userService.deleteUser("abc");


        // 조회 통해 삭제 확인
        System.out.println(userService.getAllUser());

        System.out.println("----------------------------------");

        // --- message ---
        Message msg1 = new Message("Hello!!!!", user2);
        Message msg2 = new Message("Hi!", user2);
        Message msg3 = new Message("Bye", user3);
        JCFMessageService messageService = new JCFMessageService();

        messageService.addMessage(msg1);
        messageService.addMessage(msg2);
        messageService.addMessage(msg3);

        System.out.println(messageService.getMessageFromThatUser(user2));
        System.out.println(messageService.getMessageFromThatUser(user3));
        System.out.println(messageService.getAllMessage());

        System.out.println(messageService.updateMessage(msg1, "Sorry..."));

        messageService.deleteMessage(msg3);

        System.out.println(messageService.getAllMessage());


        // --- channel ---
    }
}
