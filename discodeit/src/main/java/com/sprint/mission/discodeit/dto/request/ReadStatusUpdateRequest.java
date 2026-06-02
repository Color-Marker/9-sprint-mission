package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @Schema(description = "최근 읽음 상태 업데이트 시간")
    Instant newLastReadAt,
    boolean newNotificationEnabled
) {

}
