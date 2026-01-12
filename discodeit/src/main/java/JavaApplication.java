import entity.*;
import services.jcf.JCFChannelService;
import services.jcf.JCFMessageService;
import services.jcf.JCFServerRoomService;
import services.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JavaApplication {
    public static void main(String[] args) {
        // ---- services ----
        // 일단 맨 아래에 들어가는 msg -> channel -> server순으로 생성
        // 그래야 server에 입력 가능...
        JCFMessageService jcfMessageService = new JCFMessageService();
        JCFChannelService jcfChannelService = new JCFChannelService(jcfMessageService);
        JCFServerRoomService jcfServerRoomService = new JCFServerRoomService(jcfChannelService);

        // 유저는 서버 종속 아니니 따로
        JCFUserService jcfUserService = new JCFUserService();

        // --- user ----
        User user1 = new User("Alice", "alice@codeit.com", "01000000000");
        User user2 = new User("Bob", "bob@codeit.com", "01012345678");
        User user3 = new User("Charlie", "charlie@codeit.com", "01099999999");
        User user4 = new User("Charlie", "charlie2@codeit.com", "01088888888");

        jcfUserService.addUser(user1);
        jcfUserService.addUser(user2);
        jcfUserService.addUser(user3);
        jcfUserService.addUser(user4);

        // 유저1이 서버 생성
        ServerRoom serverRoom = new ServerRoom(user1, "Alice Server");
        jcfServerRoomService.addServerRoom(serverRoom);

        // 유저 3이 해당 서버에 참가
        jcfServerRoomService.addMemberToServer(serverRoom, user3);

        // 채널 하나 서버에 추가해줌
        Channel channel1 = new Channel(ChannelType.CHAT, "Test");
        jcfServerRoomService.addChannelToServer(serverRoom, channel1);

        // 해당 채널(channel1)에 메시지 전송
        Message channelMsg1 = new Message("Connection Check", serverRoom.getOwner());
        Message channelMsg2 = new Message("Accepted", serverRoom.getMember().get(0));
        Message channelMsg3 = new Message("This is fun", serverRoom.getMember().get(0));
        jcfChannelService.sendMessageToChannel(channel1, channelMsg1);
        jcfChannelService.sendMessageToChannel(channel1, channelMsg2);
        jcfChannelService.sendMessageToChannel(channel1, channelMsg3);

        // 등록 확인용 - 일단 오너 한 명 멤버 한 명 뿐이라 get(0)이랑 getowner로 처리
        System.out.println("Server name: " + serverRoom.getServerName());
        System.out.println("Server channel: " + serverRoom.getChannel().get(0).getChannelName());
        System.out.println("Server owner: " + serverRoom.getOwner().getDisplayName());
        System.out.println("Server member: " + serverRoom.getMember().get(0).getDisplayName());
        System.out.println("Server channel's msg");
        for(User p: serverRoom.getMember()){
            jcfChannelService.getAllMessageFromThatUser(channel1, p);
        }

        System.out.println();
        System.out.println("-----------------------------------------------------------");
        System.out.println("-----------------------------------------------------------");
        System.out.println();

        // --- 서버 테스팅 외 기본 작동 테스트 ---

        List<User> getUser1 = jcfUserService.getUserByName("Alice");
        List<User> getSameNameUsers = jcfUserService.getUserByName("Charlie");
        List<User> getAllUser = jcfUserService.getAllUser();

        jcfUserService.updateUserByName(user4, "Lucy");
        jcfUserService.updateUserByEmail(user4, "lucy@codeit.com");
        jcfUserService.updateUserByNumber(user4, "01055555555");

        jcfUserService.getAllUser();

        jcfUserService.deleteUser(user2);

        jcfUserService.getAllUser();

        System.out.println();
        System.out.println("-----------------------------------------------------------");
        System.out.println();

        // ----- message ------
        Message message1 = new Message("Mike Test", user1);
        Message message2 = new Message("Hellooooooo", user3);
        Message message3 = new Message("Hello World", user4);
        Message message4 = new Message("Bye Bye", user4);

        jcfMessageService.addMessage(message1);
        jcfMessageService.addMessage(message2);
        jcfMessageService.addMessage(message3);
        jcfMessageService.addMessage(message4);

        jcfMessageService.getMessageFromUser(user1);
        jcfMessageService.getMessageFromUser(user4);
        jcfMessageService.getMessageByContent("Hel");
        jcfMessageService.getAllMessage();

        jcfMessageService.updateMessage(message2, "Hi");

        jcfMessageService.getAllMessage();

        jcfMessageService.deleteMessage(message3);

        jcfMessageService.getAllMessage();

        System.out.println();
        System.out.println("-----------------------------------------------------------");
        System.out.println();

        // -------- channel ---------
        Channel channel2 = new Channel(ChannelType.CHAT, "Study Room");
        Channel channel3 = new Channel(ChannelType.CHAT, "Play Room");
        Channel channel4 = new Channel(ChannelType.VOICE, "Speach Room");

        jcfChannelService.addChannel(channel2);
        jcfChannelService.addChannel(channel3);
        jcfChannelService.addChannel(channel4);

        jcfChannelService.getChannelByName("Study Room");
        jcfChannelService.getAllChannel();

        jcfChannelService.updateChannelName(channel3, "Work Room");
        jcfChannelService.changeChannelType(channel4, ChannelType.CHAT);
        jcfChannelService.getAllChannel();

        jcfChannelService.deleteChannel(channel2);
        jcfChannelService.getAllChannel();
    }
}
