package com.sprint.mission.discodeit.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record MessageUpdateReqDto(
    @Schema(description = "수정된 메시지 내용")
    String newContent
) {

}
