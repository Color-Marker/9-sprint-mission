import entity.*;
import service.ChannelService;
import service.MessageService;
import service.ServerRoomService;
import service.UserService;
import service.file.FileChannelService;
import service.file.FileMessageService;
import service.file.FileServerService;
import service.file.FileUserService;

import java.util.List;

import static util.PrintUtil.classificationLine;

public class FileIOApplication {
    static User admin = new User("admin", "admin", "admin@codeit.com", "01012341234");
    static User guest = new User("guest", "guest", "guest@codeit.com", "01098765432");

    static ServerRoom serverRoom = new ServerRoom(admin, "BasicServer");

    static Channel channelA = new Channel(ChannelType.CHAT, "channel-A", serverRoom);
    static Channel channelB = new Channel(ChannelType.CHAT, "channel-B", serverRoom);

    static Message msg1 = new Message("Message 1", admin, channelA);
    static Message msg2 = new Message("Message 2", guest, channelA);
    static Message msg3 = new Message("Message 3", guest, channelB);
    static Message msg4 = new Message("Message 4", guest, channelB);

    static void userCRUDTest(UserService userService){
        // 생성
        userService.addUser(admin);
        userService.addUser(guest);
        System.out.println("User added: " + guest.getId());
        // 조회
        User target = userService.getUserById(guest.getId());
        System.out.println("Find by id: " + target.getId());
        User target2 = userService.getUserByName(guest.getDisplayName());
        System.out.println("Find by name: " + target2.getDisplayName());
        List<User> allUsers = userService.getAllUser();
        System.out.println("Number of all users: " + allUsers.size());
        // 수정
        userService.updateUserByName(guest, "IamGuest");
        userService.updateUserByPassword(guest, "IamPassword");
        userService.updateUserByEmail(guest, "guestguest@codeit.com");
        userService.updateUserByNumber(guest, "01000000000");
        User updatedGuest = userService.getUserById(guest.getId());
        System.out.println("Updated user info");
        System.out.println(updatedGuest.getDisplayName() +" " + updatedGuest.getPassword()+ " "  + updatedGuest.getEmail() + " " + updatedGuest.getPhoneNumber());
        // 삭제
        userService.deleteUser(guest);
        List<User> allUsersAfterDelete = userService.getAllUser();
        System.out.println("Deleted user");
        System.out.println("Number of all users: " + allUsersAfterDelete.size());

        classificationLine();
    }

    static void messageCRUDTest(MessageService messageService){
        // 생성
        messageService.addMessage(msg1);
        messageService.addMessage(msg2);
        messageService.addMessage(msg3);
        messageService.addMessage(msg4);
        System.out.println("Message added: " + msg1.getId());
        // 조회
        Message target = messageService.getMessageById(msg1.getId());
        System.out.println("Find by id: " + target.getId());
        messageService.getMessageByContent("1")
                .forEach(m -> System.out.println("Find by content: " + m.getId()));

        List<Message> allMsgs = messageService.getAllMessage();
        System.out.println("Number of all users: " + allMsgs.size());
        // 수정
        messageService.updateMessage(msg1, "IamAdmin");
        Message updatedMessage = messageService.getMessageById(msg1.getId());
        System.out.println("Updated message info");
        System.out.println(updatedMessage.getSender().getDisplayName() +": " + updatedMessage.getMessageContent());
        // 삭제
        messageService.deleteMessage(msg2);
        List<Message> allMsgsAfterDelete = messageService.getAllMessage();
        System.out.println("Deleted message");
        System.out.println("Number of all messages: " + allMsgsAfterDelete.size());

        classificationLine();
    }

    static void channelCRUDTest(ChannelService channelService){
        // 생성
        channelService.addChannel(channelA);
        channelService.addChannel(channelB);
        System.out.println("Channel added: " + channelA.getId());
        // 조회
        Channel target = channelService.getChannelById(channelA.getId());
        System.out.println("Find by id: " + target.getId());
        channelService.getChannelByName("channel-A")
                .forEach(c->System.out.println("Find by name: " + c.getId()));
        List<Channel> allChannel = channelService.getAllChannel();
        System.out.println("Number of all channels: " + allChannel.size());
        // 수정
        channelService.updateChannelName(channelA, "IamChannelA");
        Channel updatedChannel = channelService.getChannelById(channelA.getId());
        System.out.println("Updated channel info");
        System.out.println(updatedChannel.getChannelName() +": " + updatedChannel.getId());
        // 삭제
        channelService.deleteChannel(channelB);
        List<Channel> allChannelAfterDelete = channelService.getAllChannel();
        System.out.println("Deleted channel");
        System.out.println("Number of all channels: " + allChannelAfterDelete.size());

        classificationLine();
    }

    static void serverCRUDTest(ServerRoomService serverRoomService){
        // 생성
        serverRoomService.addServerRoom(serverRoom);
        System.out.println("Server added: " + serverRoom.getId());
        // 조회
        ServerRoom target = serverRoomService.getServerRoomByID(serverRoom.getId());
        System.out.println("Find by id: " + target.getId());
        serverRoomService.getServerByName("BasicServer")
                .forEach(s->System.out.println("Find by name: " + s.getId()));
        List<ServerRoom> allServer = serverRoomService.getAllServerRoom();
        System.out.println("Number of all servers: " + allServer.size());
        // 수정
        serverRoomService.updateServerRoom(serverRoom, "HelloServer");
        ServerRoom updatedServer = serverRoomService.getServerRoomByID(serverRoom.getId());
        System.out.println("Updated server info");
        System.out.println(updatedServer.getServerName() +": " + updatedServer.getId());
        // 삭제
        serverRoomService.deleteServerRoom(serverRoom);
        List<ServerRoom> allServerAfterDelete = serverRoomService.getAllServerRoom();
        System.out.println("Deleted server");
        System.out.println("Number of all servers: " + allServerAfterDelete.size());

        classificationLine();
    }

    public static void main(String[] args){
        MessageService messageService = new FileMessageService();
        ChannelService channelService = new FileChannelService(messageService);
        ServerRoomService serverRoomService = new FileServerService(channelService);
        UserService userService = new FileUserService(serverRoomService);

        serverCRUDTest(serverRoomService);
        userCRUDTest(userService);
        messageCRUDTest(messageService);
        channelCRUDTest(channelService);

    }
}
