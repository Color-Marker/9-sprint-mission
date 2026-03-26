package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;

public class FileWriteException extends BinaryContentException {

  public FileWriteException(Path filePath) {
    super(Instant.now(), ErrorCode.FILE_WRITE, Map.of("filePath", filePath));
  }
}
