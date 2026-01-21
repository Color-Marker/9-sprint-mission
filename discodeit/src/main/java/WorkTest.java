
import entity.*;
import service.ChannelService;
import service.MessageService;
import service.ServerRoomService;
import service.UserService;
import service.file.FileChannelService;
import service.file.FileMessageService;
import service.file.FileServerService;
import service.file.FileUserService;
import service.workTest.WorkChannelService;
import service.workTest.WorkMessageService;
import service.workTest.WorkServerRoomService;
import service.workTest.WorkUserService;

import java.util.*;

import static util.AdminUtil.isAdmin;
import static util.PrintUtil.*;

public class WorkTest {
    static MessageService messageService = new FileMessageService();
    static ChannelService channelService = new FileChannelService(messageService);
    static ServerRoomService serverRoomService = new FileServerService(channelService);
    static UserService userService = new FileUserService(serverRoomService);

    static User currentUser = null;
    static User admin;
//    static User admin = new User("admin","admin", "admin@codeit.com","01000000000");
//    static User user1 = new User("user1","user1", "user1@codeit.com","01011111111");
//    static User user2 = new User("user2","user2", "user2@codeit.com","01022222222");
//    static User user3 = new User("user3","user3", "user3@codeit.com","01033333333");

//    static ServerRoom serverRoom1 = new ServerRoom(admin, "AdminServer");
//    static ServerRoom serverRoom2 = new ServerRoom(user1, "WorkingServer");
//
//    static Channel channel1 = new Channel(ChannelType.CHAT, "AdminTest", serverRoom1);
//    static Channel channel11 = new Channel(ChannelType.CHAT, "Chat", serverRoom2);
//    static Channel channel22 = new Channel(ChannelType.VOICE, "Meeting", serverRoom2);
//
//    static Message adminMssage1 = new Message("This is Admin.", admin, channel1);
//    static Message user1Mssage = new Message("This is user1.", user1, channel11);
//    static Message user2Mssage1 = new Message("This is user2.", user2, channel11);
//    static Message user2Mssage2 = new Message("HeHe I am user2.", user2, channel22);
//    static Message user3Mssage = new Message("This is user3.", user3, channel11);
//    static Message adminMssage2 = new Message("Work checking complete.", admin, channel1);

    static Scanner sc = new Scanner(System.in);

    static {
        admin = userService.getUserByName("admin");
//        userService.addUser(admin);
//        userService.addUser(user1);
//        userService.addUser(user2);
//        userService.addUser(user3);
//        serverRoomService.addServerRoom(serverRoom1);
//        serverRoomService.addServerRoom(serverRoom2);
//        serverRoomService.addChannelToServer(serverRoom1, channel1);
//        serverRoomService.addChannelToServer(serverRoom2, channel11);
//        serverRoomService.addChannelToServer(serverRoom2, channel22);
//        serverRoomService.addMemberToServer(serverRoom2, user2);
//        serverRoomService.addMemberToServer(serverRoom2, user3);
//        channelService.sendMessageToChannel(channel1, adminMssage1);
//        channelService.sendMessageToChannel(channel1, adminMssage2);
//        channelService.sendMessageToChannel(channel11, user1Mssage);
//        channelService.sendMessageToChannel(channel11, user2Mssage1);
//        channelService.sendMessageToChannel(channel22, user2Mssage2);
//        channelService.sendMessageToChannel(channel11, user3Mssage);

    }

    public static boolean myInfoEdit(){
        while(true) {
            printMenu("!! Which one to edit? !!");
            printTypeNum("Name", "Password", "Email", "Phone Number", "Back");

            int type = Integer.parseInt(sc.nextLine().trim());
            switch (type) {
                case 1:
                    System.out.print("Name: " + currentUser.getDisplayName() + " -> ");
                    String newName = sc.nextLine().trim();
                    userService.updateUserByName(currentUser,newName);
                    System.out.println(currentUser.getDisplayName());
                    return true;
                case 2:
                    System.out.print("Type current password: ");
                    String pwCheck = sc.nextLine().trim();
                    if(currentUser.getPassword().equals(pwCheck)){
                        System.out.print("Type new password: ");
                        String newPw = sc.nextLine().trim();
                        userService.updateUserByPassword(currentUser,newPw);
                        System.out.println("! Password changed !");
                        return true;
                    }
                    else{
                        System.out.println("Wrong password!");
                        break;
                    }
                case 3:
                    System.out.print("Email: " + currentUser.getEmail() + " -> ");
                    String newEmail = sc.nextLine().trim();
                    userService.updateUserByEmail(currentUser,newEmail);
                    System.out.println(currentUser.getEmail());
                    return true;
                case 4:
                    System.out.print("Phone Number: " + currentUser.getPhoneNumber() + " -> ");
                    String newNumber = sc.nextLine().trim();
                    userService.updateUserByNumber(currentUser,newNumber);
                    System.out.println(currentUser.getPhoneNumber());
                    return true;
                case 5:
                    return false;
                default:
                    typeRightNum();
                    break;

            }
        }
    }

    public static void myInformation(){
        printMenu("My information");
        while(true){
            printTypeNum("View my information detail", "Edit my information", "Back to user");

            int type = Integer.parseInt(sc.nextLine().trim());
            switch (type) {
                case 1:
                    System.out.println(currentUser.toString());
                    break;
                case 2:
                    boolean isEdited = myInfoEdit();
                    if(isEdited){
                        System.out.println("My information is edited!");
                    }
                    else{
                        System.out.println("Nothing is changed...");
                    }
                    break;
                case 3:
                    printBack("user menu");
                    return;
                default:
                    typeRightNum();
                    break;
            }
        }
    }

    public static void workUsers(){
        printMenu("User menu");

        while(true) {
            printTypeNum("Search for user", "View all users", "My information", "Back to main", "(DANGER) Delete user");

            int type = Integer.parseInt(sc.nextLine().trim());
            switch (type) {
                case 1:
                    System.out.print("Type name of user: ");
                    String name = sc.nextLine().trim();
                    User findUser = userService.getUserByName(name);
                    printUserInfo(findUser);
                    break;
                case 2:
                    List<User> allUser = userService.getAllUser();
                    System.out.println(".... All Users ....");
                    for(User p: allUser){
                        if(p.equals(admin) && !isAdmin(currentUser,admin))
                            continue;
                        printUserInfo(p);
                    }
                    break;
                case 3:
                    myInformation();
                    break;
                case 4:
                    printBack("main menu");
                    return;
                case 5:
                    if(!isAdmin(currentUser, admin)){
                        onlyAdminCanWarning();
                    }
                    else{
                        System.out.print("Type name of user: ");
                        String delName = sc.nextLine().trim();
                        if(delName.equals(admin.getDisplayName())){
                            System.out.println("Admin can't be deleted");
                        }
                        else {
                            User findTarget = userService.getUserByName(delName);
                            if(findTarget == null){
                                System.out.println("Can't find target...");
                                break;
                            }
                            else{
                                System.out.println("Delete " + findTarget.getDisplayName());
                                userService.deleteUser(findTarget);
                            }
                        }
                    }
                    break;
                default:
                    typeRightNum();
                    break;
            }
        }
    }

    public static void workServers(){
        printMenu("Server menu");
        List<ServerRoom> serverList;

        while(true) {
            if(isAdmin(currentUser, admin)){
                System.out.println("- All server list -");
                serverList = serverRoomService.getAllServerRoom();
            }else{
                System.out.println("- Invited server list -");
                serverList = serverRoomService.getInvitedServer(currentUser);
            }
            printServerList(serverList);
            printTypeNum( "Enter to server", "Add new server", "Back to main", "(DANGER) Delete server");

            int type = Integer.parseInt(sc.nextLine().trim());
            switch (type) {
                case 1:
                    serverSelect();
                    break;
                case 2:
                    System.out.print("Type new server name: ");
                    String newName = sc.nextLine().trim();
                    serverRoomService.addServerRoom(new ServerRoom(currentUser, newName));
                    System.out.println("New server added!");
                    System.out.println();
                    break;
                case 3:
                    printBack("main menu");
                    return;
                case 4:
                    serverDel();
                    break;
                default:
                    typeRightNum();
                    break;
            }
        }
    }

    private static void serverDel() {
        List<ServerRoom> selectedServers;
        if(!isAdmin(currentUser,admin)){
            selectedServers = serverRoomService.getAllServerRoom().stream()
                .filter(s -> s.getMember().stream()
                        .anyMatch(p -> p.getId().equals(currentUser.getId())))
                .toList();
        }
        else{
            selectedServers = serverRoomService.getAllServerRoom();
        }
        System.out.print("Type server number to delete: ");
        int index = Integer.parseInt(sc.nextLine().trim());

        if (index >= 0 && index < selectedServers.size()) {
            ServerRoom target = selectedServers.get(index);
            if (target != null) {
                if(target.getOwner().getId().equals(currentUser.getId()) || isAdmin(currentUser, admin)){
                    System.out.println("Delete " + target.getServerName());
                    serverRoomService.deleteServerRoom(target);
                }
                else{
                    notAllowedWarning();
                }
            } else {
                System.out.println("Can't find target...");
            }
        } else {
            typeRightNum();
        }
    }

    private static void serverSelect() {
        List<ServerRoom> selectedServers;
        if(!isAdmin(currentUser, admin)) {
            selectedServers = serverRoomService.getAllServerRoom().stream()
                    .filter(s -> s.getMember().stream()
                            .anyMatch(p -> p.getId().equals(currentUser.getId())))
                    .toList();
        }
        else{
            selectedServers = serverRoomService.getAllServerRoom();
        }

        System.out.print("Type server number to enter: ");
        int index = Integer.parseInt(sc.nextLine().trim());

        if (index >= 0 && index < selectedServers.size()) {
            ServerRoom target = selectedServers.get(index);
            if (target != null) {
                serverEnter(target);
            } else {
                System.out.println("Can't find target...");
            }
        } else {
            typeRightNum();
        }
    }

    private static void serverEnter(ServerRoom server) {
        printMenu("Welcome to " + server.getServerName() + "!");
        while(true) {
            printServerChannel(serverRoomService, server);
            printTypeNum("Enter to channel", "View users", "Back to server menu", "(DANGER) Delete channel");
            int type = Integer.parseInt(sc.nextLine().trim());
            switch (type) {
                case 1:
                    channelSelect(server);
                    break;
                case 2:
                    printServerMember(server);
                    break;
                case 3:
                    printBack("server menu");
                    return;
                case 4:
                    channelDel(server);
                    break;
                default:
                    typeRightNum();
                    break;
            }
        }
    }

    private static void channelDel(ServerRoom server) {
        List<Channel> channels = channelService.getAllChannelByServer(server);
        System.out.print("Type channel number to enter: ");
        int index = Integer.parseInt(sc.nextLine().trim());

        if (index >= 0 && index < channels.size()) {
            Channel target = channels.get(index);
            if (target != null) {
                if(server.getOwner().getId().equals(currentUser.getId()) || isAdmin(currentUser, admin)){
                    System.out.println("Delete " + target.getChannelName());
                    channelService.deleteChannel(target);
                }
                else{
                    notAllowedWarning();
                }
            } else {
                System.out.println("Can't find target...");
            }
        } else {
            typeRightNum();
        }
    }

    private static void channelSelect(ServerRoom server) {
        List<Channel> channels = channelService.getAllChannelByServer(server);
        System.out.print("Type channel number to enter: ");
        int index = Integer.parseInt(sc.nextLine().trim());

        if (index >= 0 && index < channels.size()) {
            Channel target = channels.get(index);
            if (target != null) {
                channelEnter(target, server);
            } else {
                System.out.println("Can't find target...");
            }
        } else {
            typeRightNum();
        }
    }

    private static void channelEnter(Channel channel, ServerRoom server) {
        printMenu("Entered to " + channel.getChannelName());
        while(true) {
            printAllMsgInChannel(channelService,channel, currentUser);
            printTypeNum("Send Message", "Edit Message", "Delete Message", "Back to channel menu");
            int type = Integer.parseInt(sc.nextLine().trim());
            switch (type) {
                case 1:
                    sendMsg(channel);
                    break;
                case 2:
                    editMsg(channel);
                    break;
                case 3:
                    delMsg(channel,server);
                    break;
                case 4:
                    printBack("channel menu");
                    return;
                default:
                    typeRightNum();
                    break;
            }
        }
    }

    private static void delMsg(Channel channel, ServerRoom server) {
        List<Message> msgs = channelService.getAllMessage(channel);
        System.out.print("Type msg number to delete: ");
        int index = Integer.parseInt(sc.nextLine().trim());

        if (index >= 0 && index < msgs.size()) {
            Message target = msgs.get(index);
            if (target.getSender().getId().equals(currentUser.getId()) || server.getOwner().equals(currentUser) || isAdmin(currentUser, admin)) {
                messageService.deleteMessage(target);
                System.out.println("Deleted!");
            } else {
                notAllowedWarning();
            }
        } else {
            typeRightNum();
        }
    }

    private static void editMsg(Channel channel) {
        List<Message> msgs = channelService.getAllMessage(channel);
        System.out.print("Type msg number to edit: ");
        int index = Integer.parseInt(sc.nextLine().trim());

        if (index >= 0 && index < msgs.size()) {
            Message target = msgs.get(index);
            if (target.getSender().getId().equals(currentUser.getId()) || isAdmin(currentUser, admin)) {
                System.out.print("Type new msg content: ");
                String newContent = sc.nextLine().trim();
                messageService.updateMessage(target, newContent);
                System.out.println("Message is edited!");
            } else {
                notAllowedWarning();
            }
        } else {
            typeRightNum();
        }
    }

    private static void sendMsg(Channel channel) {
        System.out.print("Type msg: ");
        String msgContent = sc.nextLine().trim();
        channelService.sendMessageToChannel(channel, new Message(msgContent, currentUser, channel));
        System.out.println("Message is sent!");
    }

    public static User doLogin() {
        printMenu("Login process");
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();
        User buffer = userService.getUserByName(name);
        if (buffer.getPassword().equals(password)) {
            System.out.println("Welcome! " + buffer.getDisplayName());
            return buffer;
        }
        System.out.println("Check name and password");
        return null;

    }

    public static void doJoin(){
        printMenu("Join process");
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        if(userService.getUserByName(name) != null){
            System.out.println("Sorry. That is not allowed name!");
            return;
        }
        System.out.print("Password: ");
        String password = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Phone number: ");
        String number = sc.nextLine().trim();
        User newUser = new User(name, password,email,number);
        userService.addUser(newUser);
    }

    public static User whoAreYou(){
        printMenu("Please login");
        printTypeNum("Login", "Join", "Exit");

        int type = Integer.parseInt(sc.nextLine().trim());
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
                typeRightNum();
                return null;
        }
    }

    public static User workList(){
        printMenu("Main menu");
        printTypeNum("Users", "Servers", "Logout", "Exit");

        int type = Integer.parseInt(sc.nextLine().trim());
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
                typeRightNum();
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