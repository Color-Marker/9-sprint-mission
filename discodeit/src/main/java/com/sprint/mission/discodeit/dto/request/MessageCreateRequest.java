package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    @NotBlank(message = "메시지 내용은 필수입니다.")
    @Schema(description = "메시지 내용")
    String content,
    @NotNull(message = "채널 아이디는 필수입니다.")
    @Schema(description = "채널 아이디")
    UUID channelId,
    @NotNull(message = "작성자 아이디는 필수입니다.")
    @Schema(description = "작성자 아이디")
    UUID authorId
) {

}
