package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record UserUpdateDto(
        UUID userId,
        String username,
        String email,
        String password,
        BinaryContent profile
) { }
