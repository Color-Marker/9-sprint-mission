package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public record MessageCreateReqDto(
        String content,
        UUID channelId,
        UUID authorId
)
{ }
