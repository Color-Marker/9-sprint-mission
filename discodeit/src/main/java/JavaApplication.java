import entity.*;
import services.ChannelService;
import services.MessageService;
import services.ServerRoomService;
import services.UserService;

import services.jcf.JCFChannelService;
import services.jcf.JCFMessageService;
import services.jcf.JCFServerRoomService;
import services.jcf.JCFUserService;
import services.jcf.JCFServerRoomService;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JavaApplication {
    public static void main(String[] args) {
        // 서버 쪽은 작동 동안 print 굳이 안 함.
        // 기능 작동 여부 마지막에 따로 확인 정도만 진행.
        System.out.println();
        System.out.println("============= ... SErVeR TeSTinG ... =============");
        System.out.println();
        // ---- services ----
        // 일단 맨 아래에 들어가는 msg -> channel -> server순으로 생성
        // 그래야 server에 입력 가능...
        MessageService messageService = new JCFMessageService();
        ChannelService channelService = new JCFChannelService(messageService);
        ServerRoomService serverRoomService = new JCFServerRoomService(channelService);

        // 유저는 서버 종속 아니니 따로
        UserService userService = new JCFUserService();

        // --- user ----
        User user1 = new User("Alice", "alice", "alice@codeit.com", "01000000000");
        User user2 = new User("Bob", "bob","bob@codeit.com", "01012345678");
        User user3 = new User("Charlie", "charlie","charlie@codeit.com", "01099999999");
        User user4 = new User("Charlie", "charlie","charlie2@codeit.com", "01088888888");
        User user5 = new User("Harry", "harry","harry@codeit.com", "01043214321");

        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        userService.addUser(user4);
        userService.addUser(user5);

        System.out.println();

        // 유저1이 서버 생성
        ServerRoom serverRoom = new ServerRoom(user1, "Alice Server");
        serverRoomService.addServerRoom(serverRoom);

        // 유저 3,5가 해당 서버에 참가
        serverRoomService.addMemberToServer(serverRoom, user3);
        serverRoomService.addMemberToServer(serverRoom, user5);

        // 채널 하나 서버에 추가해줌
        Channel channel1 = new Channel(ChannelType.CHAT, "Test");
        serverRoomService.addChannelToServer(serverRoom, channel1);
        System.out.println();

        // 해당 채널(channel1)에 메시지 전송
        Message channelMsg1 = new Message("Connection Check", serverRoom.getOwner());
        Message channelMsg2 = new Message("Accepted", serverRoom.getMember().get(0));
        Message channelMsg3 = new Message("This is fun", serverRoom.getMember().get(1));
        channelService.sendMessageToChannel(channel1, channelMsg1);
        channelService.sendMessageToChannel(channel1, channelMsg2);
        channelService.sendMessageToChannel(channel1, channelMsg3);

        // 등록 확인용 - 일단 오너 한 명 멤버 한 명 뿐이라 get(0)이랑 getowner로 처리
        System.out.println("Server name: " + serverRoom.getServerName());
        System.out.println("Server channel: " + serverRoom.getChannel().get(0).getChannelName());
        System.out.println("Server owner: " + serverRoom.getOwner().getDisplayName());
        System.out.println("Server member1: " + serverRoom.getMember().get(0).getDisplayName());
        System.out.println("Server member2: " + serverRoom.getMember().get(1).getDisplayName());
        System.out.println("Server user's msg in channel Test");
        for(User p: serverRoom.getMember()){
            channelService.getAllMessageFromThatUser(channel1, p);
        }

        System.out.println();
        System.out.println("============= ... UsER TeSTinG ... =============");
        System.out.println();

        // --- 서버 테스팅 외 기본 작동 테스트 ---
        // user에서는 다 list로 받았지만 message, channel 부분에서는 안 받는 부분들 있음.
        // 받는 게 맞긴 한데, 출력만 확인할 용도다 보니 생략함.
        List<User> getUser1 = userService.getUserByName("Alice");
        List<User> getSameNameUsers = userService.getUserByName("Charlie");
        List<User> getAllUser = userService.getAllUser();

        // 위에 이용해서 아이디 빼내서 따로 꺼내기 가능.
        // 사실 굳이 위에 꺼 안 써도 되긴 하지만 아무래도 위에는 리스트니까
        // 이런 방식으로 단일로 빼낼 수 있다고 표현하려고..
        User getUser1ById = userService.getUserById(getUser1.get(0).getId());
        User getSameNameUser1ById = userService.getUserById(getSameNameUsers.get(0).getId());
        User getSameNameUser2ById = userService.getUserById(getSameNameUsers.get(1).getId());

        userService.updateUserByName(user4, "Lucy");
        userService.updateUserByEmail(user4, "lucy@codeit.com");
        userService.updateUserByNumber(user4, "01055555555");

        userService.getAllUser();

        userService.deleteUser(user2);

        userService.getAllUser();

        System.out.println();
        System.out.println("============= ... MeSSAge TeSTinG ... =============");
        System.out.println();

        // ----- message ------
        Message message1 = new Message("Mike Test", user1);
        Message message2 = new Message("Hellooooooo", user3);
        Message message3 = new Message("Hello World", user4);
        Message message4 = new Message("Bye Bye", user4);

        messageService.addMessage(message1);
        messageService.addMessage(message2);
        messageService.addMessage(message3);
        messageService.addMessage(message4);

        // 출력만 볼거면 굳이 use1Message 필요업긴 하지만, id 로 확인하는 거에 쓰기 위해 받음
        List<Message> user1Message = messageService.getMessageFromUser(user1);
        messageService.getMessageFromUser(user4);
        messageService.getMessageByContent("Hel");
        messageService.getMessageById(user1Message.get(0).getId());
        messageService.getAllMessage();

        messageService.updateMessage(message2, "Hi");

        messageService.getAllMessage();

        messageService.deleteMessage(message3);

        messageService.getAllMessage();

        System.out.println();
        System.out.println("============= ... ChANnel TeSTinG ... =============");
        System.out.println();

        // -------- channel ---------
        Channel channel2 = new Channel(ChannelType.CHAT, "Study Room");
        Channel channel3 = new Channel(ChannelType.CHAT, "Play Room");
        Channel channel4 = new Channel(ChannelType.VOICE, "Speach Room");

        channelService.addChannel(channel2);
        channelService.addChannel(channel3);
        channelService.addChannel(channel4);
        System.out.println();

        List<Channel> studyRoom = channelService.getChannelByName("Study Room");
        channelService.getChannelById(studyRoom.get(0).getId());
        channelService.getAllChannel();

        channelService.updateChannelName(channel3, "Work Room");
        channelService.changeChannelType(channel4, ChannelType.CHAT);
        channelService.getAllChannel();

        channelService.deleteChannel(channel2);
        channelService.getAllChannel();
    }
}
