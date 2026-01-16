package services.workTest;

import entity.ServerRoom;
import entity.User;
import services.ServerRoomService;
import services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkUserService implements UserService {

    private final List<User> data;
    private final ServerRoomService serverRoomService;

    public WorkUserService(ServerRoomService serverRoomService) {
        this.serverRoomService = serverRoomService;
        this.data = new ArrayList<>();
    }

    @Override
    public void addUser(User user) {
        boolean dupCheck;
        dupCheck = data.stream().noneMatch(p->p.getDisplayName().equals(user.getDisplayName()));
        if(dupCheck){
            data.add(user);
        }

    }

    @Override
    public User getUserById(UUID id) {
        return data.stream()
                .filter(p -> p.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public User getUserByName(String displayName) {
        return data.stream()
                .filter(p -> p.getDisplayName().equals(displayName))
                .findAny()
                .orElse(null);
    }


    @Override
    public List<User> getAllUser() {
        return new ArrayList<>(data);
    }

    @Override
    public void updateUserByName(User user, String displayName) {
        data.stream()
                .filter(p -> p.equals(user))
                .findAny()
                .map(p -> p.setDisplayName(displayName));
    }

    @Override
    public void updateUserByEmail(User user, String email) {
        data.stream()
                .filter(p -> p.equals(user))
                .findAny()
                .map(p -> p.setEmail(email));
    }

    @Override
    public void updateUserByNumber(User user, String phoneNumber) {
        data.stream()
                .filter(p -> p.equals(user))
                .findAny()
                .map(p -> p.setPhoneNumber(phoneNumber));
    }

    @Override
    public void updateUserByPassword(User user, String password) {
        data.stream()
                .filter(p -> p.equals(user))
                .findAny()
                .map(p -> p.setPassword(password));
    }

    @Override
    public void deleteUser(User user) {
        List<ServerRoom> myServers = serverRoomService.getOwningServer(user);
        for(ServerRoom s: myServers){
            serverRoomService.deleteServerRoom(s);
        }
        List<ServerRoom> memberServers = serverRoomService.getInvitedServer(user);
        for(ServerRoom s: memberServers){
            serverRoomService.delMemberInServer(s, user);
        }
        data.removeIf(p -> p.equals(user));
    }
}
