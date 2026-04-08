package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
    @NotNull
    @NotBlank(message = "아이디는 필수입니다.")
    @Schema(description = "사용자 아이디", example = "admin")
    String username,

    @NotNull
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(description = "사용자 비밀번호", example = "admin")
    String password
) {

}
