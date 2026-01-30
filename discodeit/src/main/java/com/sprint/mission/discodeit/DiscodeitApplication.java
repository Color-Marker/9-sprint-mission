package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

	static User setupUser(UserService userService) {
		BinaryContentCreateDto file1 = new BinaryContentCreateDto("file1","txt",40L);
		BinaryContentCreateDto file2 = new BinaryContentCreateDto("file2","txt",40L);
		UseCreaterDto useCreaterDto1 = new UseCreaterDto("admin", "admin@codeit.com", "admin1234", file1);
		UseCreaterDto useCreaterDto2 = new UseCreaterDto("woody", "woody@codeit.com", "woody1234", file2);
		User user1 = userService.create(useCreaterDto1);
		User user2 = userService.create(useCreaterDto2);

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
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		// 서비스 초기화
		UserService userService;
		ChannelService channelService;
		MessageService messageService;

		// TODO context에서 Bean을 조회하여 각 서비스 구현테 할당 코드 작성하기
		userService = context.getBean(UserService.class);
		channelService = context.getBean(ChannelService.class);
		messageService = context.getBean(MessageService.class);

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
