// /***
import entity.User;
import services.jcf.JCFChannelService;
import services.jcf.JCFMessageService;
import services.jcf.JCFServerRoomService;
import services.jcf.JCFUserService;
import services.workTest.WorkChannelService;
import services.workTest.WorkMessageService;
import services.workTest.WorkServerRoomService;
import services.workTest.WorkUserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class WorkTest {
    static WorkMessageService workMessageService = new WorkMessageService();
    static WorkChannelService workChannelService = new WorkChannelService(workMessageService);
    static WorkServerRoomService workServerRoomService = new WorkServerRoomService(workChannelService);
    static WorkUserService workUserService = new WorkUserService();

    static User currentUser = null;
    static User admin = new User("admin","admin", "admin@codeit.com","01000000000");
    static User user1 = new User("user1","user1", "user1@codeit.com","01011111111");
    static User user2 = new User("user2","user2", "user2@codeit.com","01022222222");
    static User user2_2 = new User("user2","user2_2", "user22@codeit.com","01033333333");

    static Scanner sc = new Scanner(System.in);

    static {
        workUserService.addUser(admin);
        workUserService.addUser(user1);
        workUserService.addUser(user2);
        workUserService.addUser(user2_2);
    }

    public static boolean myInfoEdit(){
        while(true) {
            System.out.println("!! Which one to edit? !!");
            System.out.println("1. Name / 2. Password / 3. Email / 4. Phone Number / 5. Back");
            int type = sc.nextInt();
            switch (type) {
                case 1:
                    System.out.print("Name: " + currentUser.getDisplayName() + " -> ");
                    String newName = sc.next();
                    workUserService.updateUserByName(currentUser,newName);
                    System.out.println(currentUser.getDisplayName());
                    return true;
                case 2:
                    System.out.print("Type current password: ");
                    String pwCheck = sc.next();
                    if(currentUser.getPassword().equals(pwCheck)){
                        System.out.print("Type new password: ");
                        String newPw = sc.next();
                        workUserService.updateUserByPassword(currentUser,newPw);
                        System.out.println("! Password changed !");
                        return true;
                    }
                    else{
                        System.out.println("Wrong password!");
                        break;
                    }
                case 3:
                    System.out.print("Email: " + currentUser.getEmail() + " -> ");
                    String newEmail = sc.next();
                    workUserService.updateUserByName(currentUser,newEmail);
                    System.out.println(currentUser.getEmail());
                    return true;
                case 4:
                    System.out.print("Phone Number: " + currentUser.getPhoneNumber() + " -> ");
                    String newNumber = sc.next();
                    workUserService.updateUserByNumber(currentUser,newNumber);
                    System.out.println(currentUser.getPhoneNumber());
                    return true;
                case 5:
                    return false;
                default:
                    System.out.println("Warning: Only type right numbers");
                    break;

            }
        }
    }

    public static void myInformation(){
        System.out.println("--- My Information ---");
        while(true){
            System.out.println("Type number to work");
            System.out.println("-----------------------------------");

            System.out.println("1. View my information detail");
            System.out.println("2. Edit my information");
            System.out.println("3. Back to user");

            int type = sc.nextInt();
            switch (type) {
                case 1:
                    System.out.println("... My Information ...");
                    System.out.println("Name: " + currentUser.getDisplayName());
                    System.out.println("Id: " + currentUser.getId());
                    System.out.println("Password: [CENSORED]");
                    System.out.println("Email: " + currentUser.getEmail());
                    System.out.println("Phone number: " + currentUser.getPhoneNumber());
                    System.out.println("Created time: " + currentUser.getCreatedAt());
                    System.out.println("Edited time: " + currentUser.getUpdatedAt());
                    System.out.println();
                    break;
                case 2:
                    boolean isedited = myInfoEdit();
                    if(isedited){
                        System.out.println("My information is edited!");
                    }
                    else{
                        System.out.println("Nothing is changed...");
                    }
                    break;
                case 3:
                    System.out.println("Back to user menu...");
                    return;
                default:
                    System.out.println("Warning: Only type right numbers");
                    break;
            }
        }
    }

    public static void workUsers(){
        System.out.println("--- User menu ---");

        while(true) {
            System.out.println("Type number to work");
            System.out.println("-----------------------------------");

            System.out.println("1. Search for user");
            System.out.println("2. View all users");
            System.out.println("3. My information");
            System.out.println("4. Back to main");
            System.out.println("5. (DANGER) Delete user");

            int type = sc.nextInt();
            switch (type) {
                case 1:
                    System.out.print("Type name of user: ");
                    String name = sc.next();
                    List<User> findUser = workUserService.getUserByName(name);
                    for(User p: findUser){
                        System.out.println("Name: " + p.getDisplayName());
                        System.out.println("ID: " + p.getId());
                        System.out.println();
                    }
                    break;
                case 2:
                    List<User> allUser = workUserService.getAllUser();
                    System.out.println(".... All Users ....");
                    for(User p: allUser){
                        System.out.println("Name: " + p.getDisplayName());
                        System.out.println("ID: " + p.getId());
                        System.out.println();
                    }
                    break;
                case 3:
                    myInformation();
                    break;
                case 4:
                    System.out.println("Back to main menu...");
                    return;
                case 5:
                    if(!currentUser.getId().equals(admin.getId())){
                        System.out.println("!!! Sorry! Only admin can use this! !!!");
                    }
                    else{
                        System.out.print("Type name of user: ");
                        String delName = sc.next();
                        if(delName.equals(admin.getDisplayName())){
                            System.out.println("Admin can't be deleted");
                        }
                        else {
                            List<User> findTarget = workUserService.getUserByName(delName);
                            if(findTarget.isEmpty()){
                                System.out.println("Can't find target...");
                                break;
                            }
                            if (findTarget.size() > 1) {
                                System.out.println("Many targets are found!");
                                System.out.println("Which one to delete?");
                                System.out.println();
                                for (User p : findTarget) {
                                    System.out.println(p.getDisplayName() + " " + p.getId() + " " + p.getEmail() + " " + p.getPhoneNumber());
                                }
                                System.out.println();
                                System.out.print("Type ID: ");
                                UUID delId = UUID.fromString((String)sc.next());
                                User delTarget = workUserService.getUserById(delId);
                                System.out.println("Delete user " + delTarget.getDisplayName());
                                workUserService.deleteUser(delTarget);
                            }
                            else{
                                System.out.println("Delete " + findTarget.get(0).getDisplayName());
                                workUserService.deleteUser(findTarget.get(0));
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("Warning: Only type right numbers");
                    break;
            }
        }
    }

    public static void workServers(){

    }

    public static User doLogin(){
        System.out.println("--- Login process ---");
        System.out.print("Name: ");
        String name = sc.next();
        System.out.print("Password: ");
        String password = sc.next();
        List<User> buffer = workUserService.getUserByName(name);
        for(User p: buffer){
            if(p.getPassword().equals(password)){
                System.out.println("Welcome! " + p.getDisplayName());
                return p;
            }
        }
        System.out.println("Check name and password");
        return null;

    }

    public static void doJoin(){
        System.out.println("--- Join process ---");
        System.out.print("Name: ");
        String name = sc.next();
        System.out.print("Password: ");
        String password = sc.next();
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Phone number: ");
        String number = sc.next();
        User newUser = new User(name, password,email,number);
        workUserService.addUser(newUser);
    }

    public static User whoAreYou(){
        System.out.println("Type number to work");
        System.out.println("-----------------------------------");

        System.out.println("1. Login");
        System.out.println("2. Join");
        System.out.println("3. Exit");

        int type = sc.nextInt();
        switch(type){
            case 1:
                return doLogin();
            case 2:
                doJoin();
                return null;
            case 3:
                System.out.println("Exiting...");
                System.exit(0);
                return null;
            default:
                System.out.println("Warning: Only type right numbers");
                return null;
        }
    }

    public static User workList(){
        System.out.println("--- Main menu ---");

        System.out.println("Type number to work");
        System.out.println("-----------------------------------");

        System.out.println("1. Users");
        System.out.println("2. Servers");
        System.out.println("3. Logout");
        System.out.println("4. Exit");

        int type = sc.nextInt();
        switch (type) {
            case 1:
                workUsers();
                return currentUser;
            case 2:
                workServers();
                return currentUser;
            case 3:
                System.out.println("Bye, " + currentUser.getDisplayName() + "!");
                return null;
            case 4:
                System.out.println("Exiting...");
                System.exit(0);
                return null;
            default:
                System.out.println("Warning: Only type right numbers");
                return currentUser;
        }

    }
    public static void main (String[] args){
        System.out.println();
        System.out.println("============= ... LOaDinG ... =============");
        System.out.println();

        System.out.println("Welcome to DiscodeIt!");

        while(true){
            if(currentUser == null){
                currentUser = whoAreYou();
            }
            else {
                currentUser = workList();
            }


        }
    }
}
// ***/
