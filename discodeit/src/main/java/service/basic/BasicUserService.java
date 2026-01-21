package service.basic;

import entity.ServerRoom;
import entity.User;
import repository.UserRepository;
import service.ServerRoomService;
import service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final ServerRoomService serverRoomService;
    public BasicUserService(UserRepository userRepository, ServerRoomService serverRoomService) {
        this.userRepository = userRepository;
        this.serverRoomService = serverRoomService;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUserByName(String displayName) {
        return userRepository.findAll().stream()
                .filter(p -> p.getDisplayName().equals(displayName))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void updateUserByName(User user, String displayName) {
        User target = userRepository.findById(user.getId());
        if(target == null){
            throw new NoSuchElementException("Can't find target");
        }
        target.setDisplayName(displayName);
        userRepository.save(target);
    }

    @Override
    public void updateUserByEmail(User user, String email) {
        User target = userRepository.findById(user.getId());
        if(target == null){
            throw new NoSuchElementException("Can't find target");
        }
        target.setEmail(email);
        userRepository.save(target);
    }

    @Override
    public void updateUserByNumber(User user, String phoneNumber) {
        User target = userRepository.findById(user.getId());
        if(target == null){
            throw new NoSuchElementException("Can't find target");
        }
        target.setPhoneNumber(phoneNumber);
        userRepository.save(target);
    }

    @Override
    public void updateUserByPassword(User user, String password) {
        User target = userRepository.findById(user.getId());
        if(target == null){
            throw new NoSuchElementException("Can't find target");
        }
        target.setPassword(password);
        userRepository.save(target);
    }

    @Override
    public void deleteUser(User user) {
        List<ServerRoom> myServers = serverRoomService.getOwningServer(user);
        for(ServerRoom s: myServers){
            serverRoomService.deleteServerRoom(s);
        }
        List<ServerRoom> memberServers = serverRoomService.getInvitedServer(user);
        for(ServerRoom s: memberServers) {
            serverRoomService.delMemberInServer(s, user);
        }
        userRepository.deleteById(user.getId());
    }
}
