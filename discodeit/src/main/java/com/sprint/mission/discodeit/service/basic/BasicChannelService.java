package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public Channel create(PublicChannelCreateRequest request) {
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);
    Channel saved = channelRepository.save(channel);
    log.info("public 채널 생성 완료 - 채널: {}", saved);
    return saved;
  }

  @Transactional
  @Override
  public Channel create(PrivateChannelCreateRequest request) {
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PRIVATE, name, description);
    Channel createdChannel = channelRepository.save(channel);
    log.info("private 채널 생성 완료 - 채널: {}", createdChannel);
    request.participantIds().stream()
        .map(userId -> {
          ReadStatus newStatus = new ReadStatus(userRepository.findById(userId).orElse(null),
              createdChannel,
              channel.getCreatedAt());
          log.debug("참가자별 채널 읽음 상태 생성 - 일음 상태: {}", newStatus);
          return newStatus;
        })
        .forEach(readStatusRepository::save);

    return createdChannel;
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(userId);
    }
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    List<Channel> channels;
    if (mySubscribedChannelIds.isEmpty()) {
      channels = channelRepository.findByType(ChannelType.PUBLIC);
    } else {
      channels = channelRepository.findPublicOrSubscribedChannels(mySubscribedChannelIds);
    }
    return channels.stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public Channel update(UUID channelId, PublicChannelCreateRequest request) {
    String newName = request.name();
    String newDescription = request.description();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() ->
        {
          log.warn("채널 검색 실패 - 채널 ID: {}", channelId);
          return new ChannelNotFoundException(channelId);
        });
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      log.warn("채널 수정 실패 - 채널 ID: {}", channelId);
      throw new PrivateChannelUpdateException(channelId);
    }
    channel.update(newName, newDescription);
    return channelRepository.save(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.warn("채널 검색 실패 - 채널 ID: {}", channelId);
              return new ChannelNotFoundException(channelId);
            });

    messageRepository.deleteAllByChannelId(channel.getId());
    readStatusRepository.deleteAllByChannelId(channel.getId());

    channelRepository.deleteById(channelId);
  }

}
