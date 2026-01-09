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
        System.out.println("id: " + user.getId() + " displayName: " + user.getDisplayName() + " added");
        return data.add(user);
    }

    @Override
    public User getUser(String displayName) {
        for(User p : data){
            if(p.getDisplayName().equals(displayName)){
                return p;
            }
        }
        return null;
    }

    @Override
    public List<User> getUsers(String ... displayName){
        List<User> buffer = new ArrayList<>();
        for(User p : data){
            for(String d : displayName){
                if(p.getDisplayName().equals(d)){
                    buffer.add(p);
                }
            }
        }
        if(!buffer.isEmpty()){
            return buffer;
        }else{
            return null;
        }
    }

    @Override
    public List<User> getAllUser() {
        return data;
    }

    @Override
    public User updateUser(User user, String displayName, String email, String phoneNumber) {
        for(User p : data){
            if(p.equals(user)){
                p.setDisplayName(displayName);
                p.setEmail(email);
                p.setPhoneNumber(phoneNumber);
                p.setUpdatedAt(System.currentTimeMillis());
                System.out.println("id: " + p.getId() + " new name: " + p.getDisplayName() + " new email: " + p.getEmail() + " new number: " + p.getPhoneNumber());
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean deleteUser(String displayName) {
        for(User p: data){
            if(p.getDisplayName().equals(displayName)){
                System.out.println("Delete: " + p.getId() + " name: " + p.getDisplayName());
                data.remove(p);
                return true;
            }
        }
        return false;
    }
}
