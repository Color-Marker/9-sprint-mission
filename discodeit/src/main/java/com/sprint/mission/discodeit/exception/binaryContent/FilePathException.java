package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;

public class FilePathException extends BinaryContentException {

  public FilePathException(Path filePath) {
    super(Instant.now(), ErrorCode.NOT_USABLE_FILEPATH, Map.of("filePath", filePath));
  }
}
