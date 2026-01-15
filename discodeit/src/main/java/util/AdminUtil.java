package util;

import entity.User;

public class AdminUtil {
    public static boolean isAdmin(User user, User admin){
        if(user.getId() != admin.getId()){
            return false;
        }else{
            return true;
        }
    }
}
