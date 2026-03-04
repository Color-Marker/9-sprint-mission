package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @Schema(description = "채널 아이디")
    UUID channelId,
    @Schema(description = "유저 아이디")
    UUID userId,
    @Schema(description = "최신 읽음 시간")
    Instant lastReadAt
) {

}
