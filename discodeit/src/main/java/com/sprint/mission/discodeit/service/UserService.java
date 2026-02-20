package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateReqDto;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  User create(UserCreateRequest UserCreateRequest,
      Optional<BinaryContentCreateReqDto> profileCreateRequest);

  UserDto find(UUID userId);

  List<UserDto> findAll();

  User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateReqDto> profileCreateRequest);

  void delete(UUID userId);
}
