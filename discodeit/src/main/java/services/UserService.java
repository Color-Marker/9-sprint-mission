package services;

import entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void addUser(User user);

    User getUserById(UUID id);
    User getUserByName(String displayName); // displayname은 겹치는 경우같이 여러명 가능하므로 리스트로 받음
    List<User> getAllUser();    // 그냥 모든 유저 받아오는 경우

    void updateUserByName(User user, String displayName);
    void updateUserByEmail(User user, String email);
    void updateUserByNumber(User user, String phoneNumber);
    void updateUserByPassword(User user, String password);

    void deleteUser(User user); //

}
