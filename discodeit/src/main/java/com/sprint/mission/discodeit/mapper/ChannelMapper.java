package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChannelMapper {

  @Autowired
  public MessageRepository messageRepository;
  @Autowired
  public UserRepository userRepository;
  @Autowired
  public ReadStatusRepository readStatusRepository;
  @Autowired
  public UserMapper userMapper;

  @Mapping(target = "lastMessageAt", expression = "java(findLastMessageAt(channel))")
  @Mapping(target = "participants", expression = "java(getParticipants(channel))")
  public abstract ChannelDto toDto(Channel channel);

  public Instant findLastMessageAt(Channel channel) {
    return messageRepository.findFirstByChannelOrderByCreatedAtDesc(channel)
        .map(Message::getCreatedAt)
        .orElse(null);
  }

  public List<UserDto> getParticipants(Channel channel) {
    if (channel.getType().equals(ChannelType.PUBLIC)) {
      return userRepository.findAll().stream()
          .map(userMapper::toDto)
          .toList();
    } else {
      return readStatusRepository.findAllByChannel(channel).stream()
          .map(ReadStatus::getUser)
          .map(userMapper::toDto)
          .toList();
    }
  }

}