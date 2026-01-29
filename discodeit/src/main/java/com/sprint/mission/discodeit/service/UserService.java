package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserFindResDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserDto userDto);
    UserFindResDto find(UUID userId);
    List<UserFindResDto> findAll();
    User update(UserDto userDto);
    void delete(UUID userId);
}
