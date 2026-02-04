package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;

public record PublicChannelDto (
        ChannelType type,
        String name,
        String description
){}
