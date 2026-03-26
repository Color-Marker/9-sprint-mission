package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BinaryContentCreateRequest(
    @NotBlank(message = "파일 이름은 필수입니다.")
    @Schema(description = "파일 이름")
    String fileName,
    @NotNull(message = "파일 크기는 필수입니다.")
    @Schema(description = "파일 크기")
    byte[] bytes,
    @NotNull(message = "파일 타입은 필수입니다.")
    @Schema(description = "파일 타입")
    String contentType
) {

}
