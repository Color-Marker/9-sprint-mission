package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatus create(UserStatusCreateRequest request) {
    UUID userId = request.userId();

    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(userId);
    }
    if (userStatusRepository.existsByUserId(userId)) {
      throw new UserStatusNotFoundException("userId", userId);
    }

    Instant lastActiveAt = request.lastActiveAt();
    User user = userRepository.findById(userId).orElse(null);
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    return userStatusRepository.save(userStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public UserStatus find(UUID userStatusId) {
    return userStatusRepository.findWithUserById(userStatusId)
        .orElseThrow(() -> new UserStatusNotFoundException("userStatusId", userStatusId));
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAllWithUserBy().stream()
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest) {
    Instant newLastActiveAt = Instant.now();
    if (userStatusUpdateRequest != null) {
      newLastActiveAt = userStatusUpdateRequest.lastActiveAt();
    }
    UserStatus userStatus = userStatusRepository.findWithUserById(userStatusId)
        .orElseThrow(
            () -> new UserStatusNotFoundException("userStatusId", userStatusId));
    userStatus.update(newLastActiveAt);

    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest) {
    Instant newLastActiveAt = Instant.now();
    if (userStatusUpdateRequest != null) {
      newLastActiveAt = userStatusUpdateRequest.lastActiveAt();
    }
    UserStatus userStatus = userStatusRepository.findWithUserByUserId(userId)
        .orElseThrow(
            () -> new UserStatusNotFoundException("userId", userId));
    userStatus.update(newLastActiveAt);

    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new UserStatusNotFoundException("userStatusId", userStatusId);
    }
    userStatusRepository.deleteById(userStatusId);
  }


}
