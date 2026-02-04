package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record UseCreaterDto(
        String username,
        String email,
        String password,
        BinaryContentCreateDto profile
){}
