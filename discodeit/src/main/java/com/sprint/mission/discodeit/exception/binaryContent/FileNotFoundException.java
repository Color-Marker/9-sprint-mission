package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class FileNotFoundException extends BinaryContentException {

  public FileNotFoundException(UUID fileId) {

    super(Instant.now(), ErrorCode.FILE_NOT_FOUND, Map.of("fileId", fileId));
  }
}
