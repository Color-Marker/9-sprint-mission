package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class IntegrationTest {

  @Autowired
  UserService userService;

  @Autowired
  ChannelService channelService;

  @Autowired
  MessageService messageService;

  @Test
  @DisplayName("유저 생성 - 성공")
  void createUser_success() {
    UserCreateRequest request = new UserCreateRequest("alice", "alice@codeit.com", "password123");
    UserDto user = userService.create(request, Optional.empty());

    assertThat(user.id()).isNotNull();
    assertThat(user.username()).isEqualTo("alice");
    assertThat(user.email()).isEqualTo("alice@codeit.com");
  }

  @Test
  @DisplayName("전체 유저 조회 - 성공")
  void findAllUsers_success() {
    userService.create(new UserCreateRequest("alice", "alice@codeit.com", "pw"), Optional.empty());
    userService.create(new UserCreateRequest("bob", "bob@codeit.com", "pw"), Optional.empty());

    List<UserDto> users = userService.findAll();

    assertThat(users).hasSizeGreaterThanOrEqualTo(2);
  }

  @Test
  @DisplayName("유저 수정 - 성공")
  void updateUser_success() {
    UserDto user = userService.create(
        new UserCreateRequest("alice", "alice@codeit.com", "pw"), Optional.empty());

    UserDto updated = userService.update(
        user.id(),
        new UserUpdateRequest("alice_updated", "updated@codeit.com", "newpw"),
        Optional.empty()
    );

    assertThat(updated.username()).isEqualTo("alice_updated");
    assertThat(updated.email()).isEqualTo("updated@codeit.com");
  }

  @Test
  @DisplayName("유저 삭제 - 성공")
  void deleteUser_success() {
    UserDto user = userService.create(
        new UserCreateRequest("alice", "alice@codeit.com", "pw"), Optional.empty());

    userService.delete(user.id());

    List<UserDto> users = userService.findAll();
    assertThat(users).noneMatch(u -> u.id().equals(user.id()));
  }

  @Test
  @DisplayName("public 채널 생성 - 성공")
  void createPublicChannel_success() {
    var channel = channelService.create(
        new PublicChannelCreateRequest("general", "공지 채널"));
    ChannelDto dto = channelService.find(channel.getId());

    assertThat(dto.id()).isNotNull();
    assertThat(dto.name()).isEqualTo("general");
  }

  @Test
  @DisplayName("private 채널 생성 - 성공")
  void createPrivateChannel_success() {
    UserDto user1 = userService.create(
        new UserCreateRequest("alice", "alice@codeit.com", "pw"), Optional.empty());
    UserDto user2 = userService.create(
        new UserCreateRequest("bob", "bob@codeit.com", "pw"), Optional.empty());

    var channel = channelService.create(
        new PrivateChannelCreateRequest(List.of(user1.id(), user2.id()), "dm", null));
    ChannelDto dto = channelService.find(channel.getId());

    assertThat(dto.id()).isNotNull();
    assertThat(dto.participants()).hasSize(2);
  }

  @Test
  @DisplayName("public 채널 수정 - 성공")
  void updatePublicChannel_success() {
    var channel = channelService.create(
        new PublicChannelCreateRequest("general", "공지 채널"));

    channelService.update(channel.getId(),
        new PublicChannelCreateRequest("general-updated", "수정된 설명"));

    ChannelDto dto = channelService.find(channel.getId());
    assertThat(dto.name()).isEqualTo("general-updated");
  }

  @Test
  @DisplayName("채널 삭제 - 성공")
  void deleteChannel_success() {
    UserDto user = userService.create(
        new UserCreateRequest("alice", "alice@codeit.com", "pw"), Optional.empty());
    var channel = channelService.create(
        new PublicChannelCreateRequest("general", "공지 채널"));

    channelService.delete(channel.getId());

    List<ChannelDto> channels = channelService.findAllByUserId(user.id());
    assertThat(channels).noneMatch(c -> c.id().equals(channel.getId()));
  }

  @Test
  @DisplayName("메시지 생성 - 성공")
  void createMessage_success() {
    UserDto user = userService.create(
        new UserCreateRequest("alice", "alice@codeit.com", "pw"), Optional.empty());
    var channel = channelService.create(
        new PublicChannelCreateRequest("general", "공지 채널"));

    MessageDto message = messageService.create(
        new MessageCreateRequest("hello", channel.getId(), user.id()), List.of());

    assertThat(message.id()).isNotNull();
    assertThat(message.content()).isEqualTo("hello");
    assertThat(message.channelId()).isEqualTo(channel.getId());
  }

  @Test
  @DisplayName("메시지 수정 - 성공")
  void updateMessage_success() {
    UserDto user = userService.create(
        new UserCreateRequest("alice", "alice@codeit.com", "pw"), Optional.empty());
    var channel = channelService.create(
        new PublicChannelCreateRequest("general", "공지 채널"));
    MessageDto message = messageService.create(
        new MessageCreateRequest("hello", channel.getId(), user.id()), List.of());

    MessageDto updated = messageService.update(message.id(), new MessageUpdateRequest("수정된 메시지"));

    assertThat(updated.content()).isEqualTo("수정된 메시지");
  }

  @Test
  @DisplayName("메시지 삭제 - 성공")
  void deleteMessage_success() {
    UserDto user = userService.create(
        new UserCreateRequest("alice", "alice@codeit.com", "pw"), Optional.empty());
    var channel = channelService.create(
        new PublicChannelCreateRequest("general", "공지 채널"));
    MessageDto message = messageService.create(
        new MessageCreateRequest("hello", channel.getId(), user.id()), List.of());

    messageService.delete(message.id());

    PageResponse<MessageDto> result = messageService.findAllByChannelId(
        channel.getId(), null, PageRequest.of(0, 10));
    assertThat(result.content()).noneMatch(m -> m.id().equals(message.id()));
  }

  @Test
  @DisplayName("채널 별 메시지 조회 - 성공")
  void findMessagesByChannel_success() {
    UserDto user = userService.create(
        new UserCreateRequest("alice", "alice@codeit.com", "pw"), Optional.empty());
    var channel = channelService.create(
        new PublicChannelCreateRequest("general", "공지 채널"));
    messageService.create(new MessageCreateRequest("msg1", channel.getId(), user.id()), List.of());
    messageService.create(new MessageCreateRequest("msg2", channel.getId(), user.id()), List.of());

    PageResponse<MessageDto> result = messageService.findAllByChannelId(
        channel.getId(), null, PageRequest.of(0, 10));

    assertThat(result.content()).hasSizeGreaterThanOrEqualTo(2);
  }
}
