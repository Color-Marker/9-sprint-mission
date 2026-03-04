package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record BinaryContentCreateRequest(
    @Schema(description = "파일 이름")
    String fileName,
    @Schema(description = "파일 크기")
    byte[] bytes,
    @Schema(description = "파일 타입")
    String contentType
) {

}
