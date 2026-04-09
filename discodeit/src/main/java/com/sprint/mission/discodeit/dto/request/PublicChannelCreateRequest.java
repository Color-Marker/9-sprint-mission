package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(
    @NotBlank(message = "채널 이름은 필수입니다.")
    @Schema(description = "채널 이름")
    String name,
    @NotBlank(message = "채널 정보는 필수입니다.")
    @Schema(description = "채널 정보")
    String description
) {

}
