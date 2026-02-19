package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusCreateReqDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UserStatusCreateReqDto request);

  UserStatus find(UUID userStatusId);

  List<UserStatus> findAll();

  UserStatus update(UUID userStatusId);

  UserStatus updateByUserId(UUID userId);

  void delete(UUID userStatusId);
}
