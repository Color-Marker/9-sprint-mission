package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserCreateRequest(
    @Schema(description = "이름", example = "guest")
    String username,
    @Schema(description = "이메일", example = "guest@codeit.com")
    String email,
    @Schema(description = "비밀번호", example = "guest")
    String password
) {

}
