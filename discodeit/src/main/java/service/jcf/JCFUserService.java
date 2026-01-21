package service.jcf;

import entity.ServerRoom;
import entity.User;
import service.ServerRoomService;
import service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final List<User> data;
    private final ServerRoomService serverRoomService;

    public JCFUserService(ServerRoomService serverRoomService) {
        this.serverRoomService = serverRoomService;
        this.data = new ArrayList<>();
    }


    @Override
    public void addUser(User user) {
        boolean dupCheck;
        dupCheck = data.stream().noneMatch(p->p.getDisplayName().equals(user.getDisplayName()));
        if(dupCheck){
            System.out.println("-- User add complete: " + user.getDisplayName() + " --");
            data.add(user);
        }
        else {
            System.out.println("-- Unallowed username. Try another name. --");
        }
    }


    @Override
    public User getUserById(UUID id) {
        for(User p: data){
            if(p.getId().equals(id)){
                System.out.println("-- Find user by id: " + p.getId() + " --");
                System.out.println("name: " + p.getDisplayName());
                System.out.println("email: " + p.getEmail());
                System.out.println("number: " + p.getPhoneNumber());
                System.out.println();
                return p;
            }
        }
        return null;
    }

    @Override
    public User getUserByName(String displayName) {
        for(User p: data){
            if(p.getDisplayName().equals(displayName)){
                System.out.println("-- Find user by name: " + p.getDisplayName() + " --");
                System.out.println("id: " + p.getId());
                System.out.println("email: " + p.getEmail());
                System.out.println("number: " + p.getPhoneNumber());
                System.out.println();
                return p;
            }
        }
        return null;
    }



    @Override
    public List<User> getAllUser() {
        System.out.println("-- All user information --");
        for(User p : data){
            System.out.println("id: " + p.getId());
            System.out.println("name: " + p.getDisplayName());
            System.out.println("email: " + p.getEmail());
            System.out.println("number: " + p.getPhoneNumber());
            System.out.println();
        }
        return data;
    }

    @Override
    public void updateUserByName(User user, String displayName) {
        for(User p : data) {
            if (p.getDisplayName().equals(displayName)) {
                System.out.println("-- Unallowed username. Try another name. --");
                return;
            }
        }
        for(User p: data){
            if(p.equals(user)){
                System.out.println("-- Updated user name --");
                System.out.print("User " + p.getDisplayName());
                p.setDisplayName(displayName);
                System.out.println(" changed name to " + p.getDisplayName());
                System.out.println();
                return;
            }
        }
    }
    @Override
    public void updateUserByEmail(User user, String email){
        for(User p : data){
            if(p.equals(user)){
                p.setEmail(email);
                System.out.println("-- Updated user email --");
                System.out.println("User id " + p.getId() + " whose name is " + p.getDisplayName() + " changed email to " + p.getEmail() );
                System.out.println();
                return;
            }
        }
    }

    @Override
    public void updateUserByNumber(User user, String phoneNumber){
        for(User p : data){
            if(p.equals(user)){
                p.setPhoneNumber(phoneNumber);
                System.out.println("-- Updated user number --");
                System.out.println("User id " + p.getId() + " whose name is " + p.getDisplayName() + " changed number to " + p.getPhoneNumber());
                System.out.println();
                return;
            }
        }
    }

    @Override
    public void updateUserByPassword(User user, String password) {
        for(User p : data){
            if(p.equals(user)){
                p.setPassword(password);
                return;
            }
        }
    }

    @Override
    public void deleteUser(User user) {
        List<ServerRoom> myServers = serverRoomService.getOwningServer(user);
        for(ServerRoom s: serverRoomService.getOwningServer(user)){
            serverRoomService.deleteServerRoom(s);
        }
        List<ServerRoom> memberServers = serverRoomService.getInvitedServer(user);
        for(ServerRoom s: memberServers){
            serverRoomService.delMemberInServer(s, user);
        }
        for(User p: data){
            if(p.equals(user)){
                System.out.println("-- Deleted user: " + p.getDisplayName() + " --");
                data.remove(p);
                System.out.println();
                return;
            }
        }
    }

}