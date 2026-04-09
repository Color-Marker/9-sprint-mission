package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private final Instant timestamp;
  private final String code;
  private final String message;
  private final Map<String, Object> details;
  private final String exceptionType; // 발생 예외 클래스 이름
  private final int status; // http 상태코드
}
