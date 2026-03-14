package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BinaryContentCreateReqDto(
    @Schema(description = "파일 이름")
    String fileName,
    @Schema(description = "파일 타입")
    String contentType,
    @Schema(description = "파일 용량")
    byte[] bytes
) {

}
