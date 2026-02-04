package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public record MessageCreateDto(
        String content, UUID channelId, UUID authorId,
        List<BinaryContent> files
)
{ }
