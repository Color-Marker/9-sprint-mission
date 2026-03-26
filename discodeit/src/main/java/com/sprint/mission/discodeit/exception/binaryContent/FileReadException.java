package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;

public class FileReadException extends BinaryContentException {

  public FileReadException(Path filePath) {
    super(Instant.now(), ErrorCode.FILE_READ, Map.of("filePath", filePath));
  }
}
