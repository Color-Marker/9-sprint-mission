package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class ChannelNotFoundException extends ChannelException {


  public ChannelNotFoundException(UUID channdlId) {
    super(Instant.now(), ErrorCode.DUPLICATE_NAME, Map.of("channel Id", channdlId));
  }
}
