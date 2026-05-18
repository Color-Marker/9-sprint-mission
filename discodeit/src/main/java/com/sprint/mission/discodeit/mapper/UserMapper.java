package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.DiscodeitUserDetails;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class UserMapper {

  @Autowired
  private SessionRegistry sessionRegistry;

  @Autowired
  private BinaryContentMapper binaryContentMapper;

  public UserDto toDto(User user) {
    boolean online = sessionRegistry.getAllPrincipals().stream()
        .filter(p -> p instanceof DiscodeitUserDetails)
        .map(p -> (DiscodeitUserDetails) p)
        .filter(d -> d.getUsername().equals(user.getUsername()))
        .flatMap(d -> sessionRegistry.getAllSessions(d, false).stream())
        .anyMatch(s -> !s.isExpired());

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        binaryContentMapper.toDto(user.getProfile()),
        online,
        user.getRole()
    );
  }
}