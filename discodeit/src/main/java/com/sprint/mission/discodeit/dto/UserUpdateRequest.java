package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserUpdateRequest(
    @Schema(description = "새로운 이름", example = "guest")
    String newUsername,
    @Schema(description = "새로운 이메일", example = "guest@codeit.com")
    String newEmail,
    @Schema(description = "새로운 비밀번호", example = "guest")
    String newPassword
) {

}
