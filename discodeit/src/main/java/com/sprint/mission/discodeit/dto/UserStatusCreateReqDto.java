package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateReqDto(
        UUID userId,
        Instant lastActiveAt
) { }
