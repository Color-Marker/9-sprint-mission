package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    @NotNull(message = "사용자 아이디는 필수입니다.")
    @Schema(description = "사용자 아이디", example = "guest")
    UUID userId,
    @NotNull(message = "최근 활성화 시간은 필수입니다.")
    @PastOrPresent(message = "최근 활성화 시간은 현재 또는 과거입니다.")
    @Schema(description = "최근 활성화 시간")
    Instant lastActiveAt
) {

}
