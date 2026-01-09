package services;

import entity.User;

import java.util.List;

public interface UserService {
    boolean addUser(User user);

    User getUser(String displayName);
    List<User> getAllUser();

    User updateUser(User user, String displayName, String email, String phoneNumber);

    boolean deletedUser(String displayName);
}
