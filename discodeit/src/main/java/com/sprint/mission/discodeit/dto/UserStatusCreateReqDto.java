package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateReqDto(
    @Schema(description = "사용자 아이디", example = "guest")
    UUID userId,
    @Schema(description = "최근 활성화 시간")
    Instant lastActiveAt
) {

}
