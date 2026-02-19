package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLoginReqDto(
    @Schema(description = "사용자 아이디", example = "admin")
    String username,
    @Schema(description = "사용자 비밀번호", example = "admin")
    String password
) {

}
