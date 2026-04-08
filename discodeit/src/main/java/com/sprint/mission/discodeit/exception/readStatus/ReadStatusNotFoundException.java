package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(UUID readStautsId) {
    super(Instant.now(), ErrorCode.READSTATUS_NOT_FOUND, Map.of("readStatusId", readStautsId));
  }
}
