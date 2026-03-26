package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final PageResponseMapper pageResponseMapper;
  private final MessageMapper messageMapper;

  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
    }
    if (!userRepository.existsById(authorId)) {
      throw new NoSuchElementException("Author with id " + authorId + " does not exist");
    }
    List<UUID> attachmentIds = new ArrayList<>();
    if (!binaryContentCreateRequests.isEmpty()) {
      attachmentIds = binaryContentCreateRequests.stream()
          .map(attachmentRequest -> {
            String fileName = attachmentRequest.fileName();
            String contentType = attachmentRequest.contentType();
            byte[] bytes = attachmentRequest.bytes();

            BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                contentType);
            BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
            binaryContentStorage.put(createdBinaryContent.getId(), attachmentRequest.bytes());
            return createdBinaryContent.getId();
          })
          .toList();
    }
    String content = messageCreateRequest.content();
    Channel channel = channelRepository.findById(channelId).orElse(null);
    User author = userRepository.findById(authorId).orElse(null);
    List<BinaryContent> attachments = binaryContentRepository.findAllByIdIn(attachmentIds);
    Message message = new Message(
        content,
        channel,
        author,
        attachments
    );
    messageRepository.save(message);
    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  @Override
  public Message find(UUID messageId) {
    return messageRepository.findWithChannelAuthorAttachmentById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor,
      Pageable pageable) {
    Slice<Message> messageSlice;
    if (cursor == null) {
      messageSlice = messageRepository.findAllWithExtraByChannelId(channelId, pageable);
    } else {
      messageSlice = messageRepository.findAllByCursor(channelId, cursor, pageable);
    }

    return pageResponseMapper.fromSlice(
        messageSlice,
        message -> messageMapper.toDto(message),
        message -> message.getCreatedAt()
    );
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findWithChannelAuthorAttachmentById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    messageRepository.save(message);
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    if (!messageRepository.existsById(messageId)) {
      throw new NoSuchElementException("Message with id " + messageId + " not found");
    }
    messageRepository.deleteById(messageId);
  }

}
