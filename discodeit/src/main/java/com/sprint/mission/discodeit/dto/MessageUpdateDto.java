package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record MessageUpdateDto(
        UUID messageId, String newContent
) {
}
