package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record ChannelUpdateDto(
        UUID channelId,
        String newName,
        String newDescription
) {
}
