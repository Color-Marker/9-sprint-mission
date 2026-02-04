package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserFindResDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateDto userCreateDto);
    UserFindResDto find(UUID userId);
    List<UserFindResDto> findAll();
    User update(UserUpdateDto userUpdateDto);
    void delete(UUID userId);
}
