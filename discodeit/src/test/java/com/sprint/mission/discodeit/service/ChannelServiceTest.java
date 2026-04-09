package com.sprint.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

  @InjectMocks
  private BasicChannelService channelService;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ChannelMapper channelMapper;


  @Test
  @DisplayName("PUBLIC 채널 생성 성공 테스트")
  void createPublicChannelSuccessTest() {
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.builder()
        .id(channelId)
        .type(ChannelType.PUBLIC).name("public test").description("public test").build();
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("public test",
        "public test");
    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    Channel result = channelService.create(request);
    assertEquals(result, channel);
    then(channelRepository).should().save(any(Channel.class));
  }

  @Test
  @DisplayName("PRIVATE 채널 생성 성공 테스트")
  void createPrivateChannelSuccessTest() {
    UUID channelId = UUID.randomUUID();
    List<UUID> userIds = new ArrayList<>();
    userIds.add(UUID.randomUUID());
    userIds.add(UUID.randomUUID());
    Channel channel = Channel.builder()
        .id(channelId)
        .type(ChannelType.PRIVATE).name("private test").description("private test").build();
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(userIds, "private test",
        "private test");
    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    Channel result = channelService.create(request);
    assertEquals(result, channel);
    then(channelRepository).should().save(any(Channel.class));
  }

  @Test
  @DisplayName("채널 수정 성공 테스트")
  void updateChannelSuccessTest() {
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.builder()
        .id(channelId)
        .type(ChannelType.PUBLIC).name("public test").description("public test").build();
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("public update test",
        "public update test");
    Channel updatedChannel = Channel.builder()
        .id(channelId)
        .type(ChannelType.PUBLIC).name("public update test").description("public update test")
        .build();
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(channelRepository.save(any(Channel.class))).willReturn(updatedChannel);
    Channel result = channelService.update(channelId, request);
    assertEquals(result, updatedChannel);
    then(channelRepository).should().save(channel);
  }

  @Test
  @DisplayName("채널 수정 실패 테스트 - 검색 실패")
  void updateChannelFindFailTest() {
    UUID channelId = UUID.randomUUID();
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());
    assertThrows(ChannelNotFoundException.class, () -> {
      channelService.update(channelId, new PublicChannelCreateRequest("이름", "설명"));
    });
    then(channelRepository).should(never()).save(any(Channel.class));
  }

  @Test
  @DisplayName("채널 수정 실패 테스트 - private 채널은 수정 불가")
  void updateChannelPrivateFailTest() {
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.builder()
        .id(channelId)
        .type(ChannelType.PRIVATE).name("private test").description("private test").build();
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    assertThrows(PrivateChannelUpdateException.class, () -> {
      channelService.update(channelId, new PublicChannelCreateRequest("이름", "설명"));
    });
    then(channelRepository).should(never()).save(any(Channel.class));
  }

  @Test
  @DisplayName("채널 삭제 성공 테스트")
  void deleteChannelSuccessTest() {
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.builder()
        .id(channelId)
        .type(ChannelType.PUBLIC).name("test").description("test").build();
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    channelService.delete(channelId);

    then(channelRepository).should().findById(channelId);
    then(messageRepository).should().deleteAllByChannelId(channelId);
    then(readStatusRepository).should().deleteAllByChannelId(channelId);
    then(channelRepository).should().deleteById(channelId);
  }

  @Test
  @DisplayName("채널 삭제 실패 테스트 - 검색 실패")
  void deleteChannelFailTest() {
    UUID channelId = UUID.randomUUID();
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());
    assertThrows(ChannelNotFoundException.class, () -> {
      channelService.delete(channelId);
    });
    then(channelRepository).should().findById(channelId);
    then(messageRepository).shouldHaveNoInteractions();
    then(readStatusRepository).shouldHaveNoInteractions();
    then(channelRepository).should(never()).deleteById(any(UUID.class));
  }

  @Test
  @DisplayName("채널 검색 성공 테스트")
  void findChannelSuccessTest() {
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.builder()
        .id(channelId)
        .type(ChannelType.PUBLIC).name("test").description("test").build();
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(channelMapper.toDto(channel)).willReturn(
        new ChannelDto(channelId, ChannelType.PUBLIC, "test", "test", List.of(), null));
    channelService.find(channelId);
    then(channelRepository).should().findById(channelId);
  }

  @Test
  @DisplayName("채널 검색 실패 테스트 - 존재하지 않는 채널")
  void findChannelFailTest() {
    UUID channelId = UUID.randomUUID();
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());
    assertThrows(ChannelNotFoundException.class, () -> {
      channelService.find(channelId);
    });
  }
}
