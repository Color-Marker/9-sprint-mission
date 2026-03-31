package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

  @InjectMocks
  private BasicMessageService messageService;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private PageResponseMapper pageResponseMapper;
  @Mock
  private MessageMapper messageMapper;

  @Test
  @DisplayName("메시지 생성 성공 테스트")
  void msgCreateSuccessTest() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    UUID profileId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("안녕!", channelId, authorId);
    BinaryContentCreateRequest fileRequest = new BinaryContentCreateRequest("test.png",
        new byte[]{1, 2, 3}, "image/png");
    BinaryContent profile = BinaryContent.builder()
        .fileName("test.png").size((long) fileRequest.bytes().length).contentType("image/png")
        .id(profileId).build();
    Channel channel = Channel.builder()
        .id(channelId)
        .type(ChannelType.PUBLIC).name("채널").description("채널")
        .build();
    User user = User.builder()
        .id(authorId)
        .username("test").email("test@test.com").password("test").profile(profile).build();
    List<BinaryContentCreateRequest> fileRequests = List.of(fileRequest);
    given(channelRepository.existsById(channelId)).willReturn(true);
    given(userRepository.existsById(authorId)).willReturn(true);
    BinaryContent mockFile = BinaryContent.builder().id(UUID.randomUUID()).build();
    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(mockFile);
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(user));
    given(binaryContentRepository.findAllByIdIn(any())).willReturn(List.of(mockFile));
    messageService.create(request, fileRequests);
    then(binaryContentRepository).should().save(any(BinaryContent.class));
    then(binaryContentStorage).should().put(eq(mockFile.getId()), any(byte[].class));
    then(messageRepository).should().save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 생성 실패 테스트 - 채널 검색 실패")
  void msgCreateFindChannelFailTest() {
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(false);
    assertThrows(ChannelNotFoundException.class, () -> {
      messageService.create(new MessageCreateRequest("내용", channelId, UUID.randomUUID()),
          List.of());
    });
    then(userRepository).should(never()).existsById(any());
    then(messageRepository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("메시지 생성 실패 테스트 - 작성자 검색 실패")
  void msgCreateFindUserFailTest() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(true);
    given(userRepository.existsById(authorId)).willReturn(false);
    assertThrows(UserNotFoundException.class, () -> {
      messageService.create(new MessageCreateRequest("내용", channelId, authorId),
          List.of());
    });

    then(channelRepository).should().existsById(any());
    then(userRepository).should().existsById(any());
    then(messageRepository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("채널 내 모든 메시지 검색 테스트 - 커서 없음")
  void findAllMsgByChannelIdNoCursorTest() {
    UUID channelId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 10);
    Slice<Message> messageSlice = new SliceImpl<>(List.of());
    given(messageRepository.findAllWithExtraByChannelId(channelId, pageable))
        .willReturn(messageSlice);
    given(pageResponseMapper.fromSlice(any(), any(), any()))
        .willReturn(new PageResponse<>(List.of(), null, 10, false, 0L));
    messageService.findAllByChannelId(channelId, null, pageable);
    then(messageRepository).should().findAllWithExtraByChannelId(channelId, pageable);
    then(messageRepository).should(never()).findAllByCursor(any(), any(), any());
  }

  @Test
  @DisplayName("채널 내 모든 메시지 검색 테스트 - 커서 존재")
  void findAllMsgByChannelIdWithCursorTest() {
    UUID channelId = UUID.randomUUID();
    Instant cursor = Instant.now();
    Pageable pageable = PageRequest.of(0, 10);
    Slice<Message> messageSlice = new SliceImpl<>(List.of());

    given(messageRepository.findAllByCursor(channelId, cursor, pageable))
        .willReturn(messageSlice);

    given(pageResponseMapper.fromSlice(any(), any(), any()))
        .willReturn(new PageResponse<>(List.of(), null, 10, false, 0L));
    messageService.findAllByChannelId(channelId, cursor, pageable);
    then(messageRepository).should().findAllByCursor(channelId, cursor, pageable);
    then(messageRepository).should(never()).findAllWithExtraByChannelId(any(), any());
  }

  @Test
  @DisplayName("메시지 수정 성공 테스트")
  void msgUpdateSuccessTest() {
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("수정 내용");
    Message message = Message.builder()
        .id(messageId)
        .content("원래 내용")
        .build();
    Message updateMessage = Message.builder()
        .id(messageId)
        .content("수정 내용")
        .build();
    given(messageRepository.findWithChannelAuthorAttachmentById(messageId)).willReturn(
        Optional.of(message));
    given(messageRepository.save(any(Message.class))).willReturn(message);
    MessageDto result = messageService.update(messageId, request);
    MessageDto expect = messageMapper.toDto(updateMessage);
    assertEquals(result, expect);
    then(messageRepository).should().save(message);
  }

  @Test
  @DisplayName("메시지 수정 실패 테스트 - 메시지 검색 실패")
  void msgUpdateFindFailTest() {
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("수정 시도");
    given(messageRepository.findWithChannelAuthorAttachmentById(messageId)).willReturn(
        Optional.empty());
    assertThrows(MessageNotFoundException.class, () -> {
      messageService.update(messageId, request);
    });
    then(messageRepository).should().findWithChannelAuthorAttachmentById(messageId);
    then(messageRepository).should(never()).save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 삭제 성공 테스트")
  void msgDelSuccessTest() {
    UUID messageId = UUID.randomUUID();
    given(messageRepository.existsById(messageId)).willReturn(true);
    messageService.delete(messageId);
    then(messageRepository).should().existsById(messageId);
    then(messageRepository).should().deleteById(messageId);
  }

  @Test
  @DisplayName("메시지 삭제 실패 테스트 - 메시지 검색 실패")
  void msgDelFindFailTest() {
    UUID messageId = UUID.randomUUID();
    given(messageRepository.existsById(messageId)).willReturn(false);
    assertThrows(MessageNotFoundException.class, () -> {
      messageService.delete(messageId);
    });
    then(messageRepository).should().existsById(messageId);
    then(messageRepository).should(never()).deleteById(any(UUID.class));
  }
}
