package com.sprint.mission.discodeit.dto;

public record UserFindResDto(
        String username,
        String email,
        boolean onlineStatus
){}
