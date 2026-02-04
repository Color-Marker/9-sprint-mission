package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record ReadStatusUpdateDto(
        UUID id,
        boolean isRead
) { }
