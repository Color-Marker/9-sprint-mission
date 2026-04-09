package com.sprint.mission.discodeit.exception.userStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class UserStatusAlreadyExistException extends UserStatusException {

  public UserStatusAlreadyExistException(UUID userId) {
    super(Instant.now(), ErrorCode.USERSTATUS_ALREADY_EXIST, Map.of("userId", userId));
  }
}
