package com.sprint.mission.discodeit.dto;

public record UserCreateReqDto(
        String username,
        String email,
        String password
){}
