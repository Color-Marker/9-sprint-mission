package services;

import entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    boolean addUser(User user);

    List<User> getUserByName(String displayName); // displayname은 겹치는 경우같이 여러명 가능하므로 리스트로 받음
    List<User> getAllUser();    // 그냥 모든 유저 받아오는 경우

    boolean updateUserByName(User user, String displayName);
    boolean updateUserByEmail(User user,String email);
    boolean updateUserByNumber(User user, String phoneNumber);

    boolean deleteUser(User user); //

}
