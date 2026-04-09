package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

public class DuplicateNameException extends UserException {

  public DuplicateNameException(String name) {
    super(Instant.now(), ErrorCode.DUPLICATE_NAME, Map.of("username", name));
  }
}
