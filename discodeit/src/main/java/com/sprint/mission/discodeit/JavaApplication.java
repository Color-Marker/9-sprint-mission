package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelDto;
import com.sprint.mission.discodeit.dto.UseCreaterDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JavaApplication {
    static User setupUser(UserService userService) {
        UseCreaterDto useCreaterDto = new UseCreaterDto("woody", "woody@codeit.com", "woody1234", null);
        return userService.create(useCreaterDto);
    }

    static Channel setupChannel(ChannelService channelService) {
        PublicChannelDto publicChannelDto = new PublicChannelDto(ChannelType.PUBLIC, "notice", "This is notice channel.");
        return channelService.createPublicChannel(publicChannelDto);
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
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}
