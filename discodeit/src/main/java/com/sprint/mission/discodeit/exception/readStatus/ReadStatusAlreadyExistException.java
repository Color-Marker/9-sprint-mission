package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class ReadStatusAlreadyExistException extends ReadStatusException {

  public ReadStatusAlreadyExistException(UUID userId, UUID channelId) {
    super(Instant.now(), ErrorCode.READSTATUS_ALREADY_EXIST,
        Map.of("userId", userId, "channelId", channelId));
  }
}
