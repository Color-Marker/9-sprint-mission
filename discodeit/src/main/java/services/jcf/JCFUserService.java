package services.jcf;

import entity.User;
import services.UserService;

import java.util.ArrayList;
import java.util.List;

public class JCFUserService implements UserService{

    private final List<User> data;

    public JCFUserService(){
        this.data = new ArrayList<>();
    }

    @Override
    public boolean addUser(User user) {
        return data.add(user);
    }

    @Override
    public User getUser(String displayName) {
        return null;
    }

//    @Override
//    public User getUser(String displayName) {
//        return data;
//    }

    @Override
    public List<User> getAllUser() {
        return data;
    }

    @Override
    public User updateUser(User user, String displayName, String email, String phoneNumber) {
        return null;
    }

    @Override
    public boolean deletedUser(String displayName) {
        return false;
    }
}
