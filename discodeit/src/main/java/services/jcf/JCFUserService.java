package services.jcf;

import entity.User;
import services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final List<User> data;

    public JCFUserService() {
        this.data = new ArrayList<>();
    }

    @Override
    public boolean addUser(User user) {
        System.out.println("-- User add complete: " + user.getDisplayName() + " --");
        return data.add(user);
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
    public List<User> getUserByName(String displayName) {
        List<User> buffer = new ArrayList<>();
        for(User p : data){
            if(p.getDisplayName().equals(displayName)){
                System.out.println("-- Find user by name: " + p.getDisplayName() + " --");
                System.out.println("id: " + p.getId());
                System.out.println("email: " + p.getEmail());
                System.out.println("number: " + p.getPhoneNumber());
                System.out.println();
                buffer.add(p);
            }
        }
        return buffer;
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
    public boolean updateUserByName(User user, String displayName) {
        for(User p : data){
            if(p.equals(user)){
                System.out.println("-- Updated user name --");
                System.out.print("User " + p.getDisplayName());
                p.setDisplayName(displayName);
                System.out.println(" changed name to " + p.getDisplayName());
                System.out.println();
                p.setUpdatedAt();
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean updateUserByEmail(User user,String email){
        for(User p : data){
            if(p.equals(user)){
                p.setEmail(email);
                System.out.println("-- Updated user email --");
                System.out.println("User id " + p.getId() + " whose name is " + p.getDisplayName() + " changed email to " + p.getEmail() );
                System.out.println();
                p.setUpdatedAt();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateUserByNumber(User user, String phoneNumber){
        for(User p : data){
            if(p.equals(user)){
                p.setPhoneNumber(phoneNumber);
                System.out.println("-- Updated user number --");
                System.out.println("User id " + p.getId() + " whose name is " + p.getDisplayName() + " changed number to " + p.getPhoneNumber());
                System.out.println();
                p.setUpdatedAt();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateUserByPassword(User user, String password) {
        for(User p : data){
            if(p.equals(user)){
                p.setPassword(password);
                p.setUpdatedAt();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteUser(User user) {
        for(User p: data){
            if(p.equals(user)){
                System.out.println("-- Deleted user: " + p.getDisplayName() + " --");
                data.remove(p);
                System.out.println();
                return true;
            }
        }
        return false;
    }
}
