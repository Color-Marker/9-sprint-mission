package services;

import entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    boolean addUser(User user);

    List<User> getUserByName(String displayName); // displayname은 겹치는 경우같이 여러명 가능하므로 리스트로 받음
    User getUserByID(UUID id); // uuid id는 안 겹치는 유일한 경우일테니 한 개의 user만 리턴하면 됌

    List<User> getAllUser();    // 그냥 모든 유저 받아오는 경우

    User updateUser(User user, String displayName, String email, String phoneNumber);

    boolean deleteUser(String displayName);


}
