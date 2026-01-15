package services.workTest;

import entity.User;
import services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WorkUserService implements UserService {

    private final List<User> data;

    public WorkUserService() {
        this.data = new ArrayList<>();
    }

    @Override
    public boolean addUser(User user) {
        return data.add(user);
    }

    @Override
    public User getUserById(UUID id) {
        return data.stream()
                .filter(p -> p.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<User> getUserByName(String displayName) {
        return data.stream()
                .filter(p -> p.getDisplayName().equals(displayName))
                .toList();
    }


    @Override
    public List<User> getAllUser() {
        return new ArrayList<>(data);
    }

    @Override
    public boolean updateUserByName(User user, String displayName) {
        return data.stream()
                .filter(p -> p.equals(user))
                .findAny()
                .map(p ->p.setDisplayName(displayName))
                .orElse(false);
    }

    @Override
    public boolean updateUserByEmail(User user, String email) {
        return data.stream()
                .filter(p -> p.equals(user))
                .findAny()
                .map(p -> p.setEmail(email))
                .orElse(false);
    }

    @Override
    public boolean updateUserByNumber(User user, String phoneNumber) {
        return data.stream()
                .filter(p -> p.equals(user))
                .findAny()
                .map(p ->p.setPhoneNumber(phoneNumber))
                .orElse(false);
    }

    @Override
    public boolean updateUserByPassword(User user, String password) {
        return data.stream()
                .filter(p -> p.equals(user))
                .findAny()
                .map(p -> p.setPassword(password))
                .orElse(false);
    }

    @Override
    public boolean deleteUser(User user) {
        return data.removeIf(p -> p.equals(user));
    }
}
