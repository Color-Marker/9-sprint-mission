package com.sprint.mission.discodeit.dto;

import java.time.LocalDateTime;

public record ResponseDto<T>(
    boolean status,
    T data,
    Object error,
    LocalDateTime timestamp
) {

  public static <T> ResponseDto<T> ok(T data) {
    return new ResponseDto<>(true, data, null, LocalDateTime.now());
  }

  public static <T> ResponseDto<T> fail(Object error) {
    return new ResponseDto<>(false, null, error, LocalDateTime.now());
  }

}
