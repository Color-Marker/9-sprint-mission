package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    @Schema(description = "사용자 아이디", example = "guest")
    UUID userId,
    @Schema(description = "최근 활성화 시간")
    Instant lastActiveAt
) {

}
