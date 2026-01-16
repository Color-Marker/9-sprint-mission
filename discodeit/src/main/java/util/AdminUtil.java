package util;

import entity.User;

public class AdminUtil {
    public static boolean isAdmin(User user, User admin){
        return user.getId() == admin.getId();
    }
}
