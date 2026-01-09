import entity.User;
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

        // --- message ---


        // --- channel ---
    }
}
