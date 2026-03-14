package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record ResponseDto<T>(
    @Schema(description = "상태")
    boolean status,
    @Schema(description = "데이터")
    T data,
    @Schema(description = "에러")
    Object error,
    @Schema(description = "작업 시간")
    LocalDateTime timestamp
) {

  public static <T> ResponseDto<T> ok(T data) {
    return new ResponseDto<>(true, data, null, LocalDateTime.now());
  }

  public static <T> ResponseDto<T> fail(Object error) {
    return new ResponseDto<>(false, null, error, LocalDateTime.now());
  }

}
