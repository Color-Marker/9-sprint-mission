package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull(message = "채널 아이디는 필수입니다.")
    @Schema(description = "채널 아이디")
    UUID channelId,
    @NotNull(message = "유저 아이디는 필수입니다.")
    @Schema(description = "유저 아이디")
    UUID userId,
    @Schema(description = "최신 읽음 시간")
    Instant lastReadAt
) {

}
