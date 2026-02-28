package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record UserUpdateReqDto(
        String username,
        String email,
        String password
) { }
