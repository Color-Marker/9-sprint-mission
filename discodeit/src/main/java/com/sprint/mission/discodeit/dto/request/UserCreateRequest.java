package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요")
    @Pattern(regexp = "^[가-힣a-zA-Z\\s]+$",
        message = "이름은 한글, 영문, 공백만 사용할 수 있습니다")
    @Schema(description = "이름", example = "guest")
    String username,
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바르 이메일 형식이 아닙니다.")
    @Schema(description = "이메일", example = "guest@codeit.com")
    String email,
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(description = "비밀번호", example = "guest")
    String password
) {

}
