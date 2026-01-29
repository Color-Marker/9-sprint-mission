package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
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
        BinaryContentCreateDto file = new BinaryContentCreateDto("file","txt",40L);
        UseCreaterDto useCreaterDto = new UseCreaterDto("woody", "woody@codeit.com", "woody1234", file);
        return userService.create(useCreaterDto);
    }

    static Channel setupPublicChannel(ChannelService channelService) {
        PublicChannelDto publicChannelDto = new PublicChannelDto(ChannelType.PUBLIC, "notice", "This is notice channel.");
        return channelService.createPublicChannel(publicChannelDto);
    }

    static Channel setupPrivateChannel(ChannelService channelService, List<UUID> userIdList) {
        PrivateChannelDto privateChannelDto = new PrivateChannelDto(ChannelType.PRIVATE, userIdList);
        return channelService.createPrivateChannel(privateChannelDto);
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        MessageCreateDto messageCreateDto = new MessageCreateDto("Hello World", channel.getId(), author.getId(), null);
        Message message = messageService.create(messageCreateDto);
        System.out.println("Message created: " + message.getId());
    }

    public static void main(String[] args) {
        // 레포지토리 초기화
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();
        BinaryContentRepository binaryContentRepository = new FileBinaryContentRepository();
        ReadStatusRepository readStatusRepository = new FileReadStatusRepository();
        UserStatusRepository userStatusRepository = new FileUserStatusRepository();

        // 서비스 초기화
        UserService userService = new BasicUserService(userRepository, userStatusRepository, binaryContentRepository);
        ChannelService channelService = new BasicChannelService(channelRepository,readStatusRepository, messageRepository);
        MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository, binaryContentRepository);

        // 셋업
        User user = setupUser(userService);

        List<User> userList = new ArrayList<>();
        userList.add(user);
        List<UUID> userIdList = userList.stream()
                .map(User::getId).toList();
        Channel publicChannel = setupPublicChannel(channelService);
        Channel privateChannel = setupPrivateChannel(channelService, userIdList);
        // 테스트
        messageCreateTest(messageService, publicChannel, user);
        messageCreateTest(messageService, privateChannel, user);
    }
}
