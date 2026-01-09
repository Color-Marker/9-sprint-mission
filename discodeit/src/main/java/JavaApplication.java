import entity.User;
import services.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        User user = new User("abc", "abc@codeit.com", "01099999999");
        JCFUserService userService = new JCFUserService();

        if(userService.addUser(user)){
            System.out.println("Add successed");
        }else{
            System.out.println("Add failed");
        }

        System.out.println(userService.getAllUser());

    }
}
