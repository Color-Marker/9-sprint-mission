package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JavaApplication {
    static User setupUser(UserService userService) {
        BinaryContentCreateDto file1 = new BinaryContentCreateDto("file1","txt",40L);
        BinaryContentCreateDto file2 = new BinaryContentCreateDto("file2","txt",40L);
        UserCreateDto userCreateDto1 = new UserCreateDto("admin", "admin@codeit.com", "admin1234", file1);
        UserCreateDto userCreateDto2 = new UserCreateDto("woody", "woody@codeit.com", "woody1234", file2);
        User user1 = userService.create(userCreateDto1);
        User user2 = userService.create(userCreateDto2);

        System.out.println("user1: " + user1.getUsername() + "_" + user1.getId());
        System.out.println("user2: " + user2.getUsername() + "_" + user2.getId());

        return user1;
    }

    static Channel setupPublicChannel(ChannelService channelService) {
        PublicChannelDto publicChannelDto = new PublicChannelDto(ChannelType.PUBLIC, "public", "This is public channel.");
        return channelService.createPublicChannel(publicChannelDto);
    }

    static Channel setupPrivateChannel(ChannelService channelService, List<UUID> userIdList) {
        PrivateChannelDto privateChannelDto = new PrivateChannelDto(ChannelType.PRIVATE, "private", "This is private channel", userIdList);
        return channelService.createPrivateChannel(privateChannelDto);
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        MessageCreateDto messageCreateDto = new MessageCreateDto("Hello World", channel.getId(), author.getId(), null);
        Message message = messageService.create(messageCreateDto);
        System.out.println("Message created: " + message.getContent());
        System.out.println("In channel: " + message.getChannelId());
        System.out.println("By user: " + message.getAuthorId());
        System.out.println();
    }

    public static void main(String[] args) {
        // 레포지토리 초기화
        UserRepository userRepository = new JCFUserRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();
        MessageRepository messageRepository = new JCFMessageRepository();
        BinaryContentRepository binaryContentRepository = new JCFBinaryContentRepository();
        ReadStatusRepository readStatusRepository = new JCFReadStatusRepository();
        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();

        // 서비스 초기화a
        UserService userService = new BasicUserService(userRepository, userStatusRepository, binaryContentRepository);
        ChannelService channelService = new BasicChannelService(channelRepository,readStatusRepository, messageRepository);
        MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository, binaryContentRepository);

        // 셋업
        User user = setupUser(userService);
        System.out.println();

        List<User> userList = new ArrayList<>();
        userList.add(user);
        List<UUID> userIdList = userList.stream()
                .map(User::getId).toList();
        Channel publicChannel = setupPublicChannel(channelService);
        System.out.println("Public Channel: " + publicChannel.getName() + "_" + publicChannel.getId());
        Channel privateChannel = setupPrivateChannel(channelService, userIdList);
        System.out.println("Private Channel: " + privateChannel.getName() + "_" + privateChannel.getId());
        System.out.println();

        // 테스트
        messageCreateTest(messageService, publicChannel, user);
        messageCreateTest(messageService, privateChannel, user);
    }
}
