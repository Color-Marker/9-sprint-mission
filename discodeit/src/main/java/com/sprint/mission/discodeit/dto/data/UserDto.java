package com.sprint.mission.discodeit.dto.data;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record UserDto(
    @Schema(description = "유저 아이디")
    UUID id,
    @Schema(description = "유저 생성 일자")
    Instant createdAt,
    @Schema(description = "유저 업데이트 일자")
    Instant updatedAt,
    @Schema(description = "유저 이름")
    String username,
    @Schema(description = "유저 이메일")
    String email,
    @Schema(description = "프로필 파일 아이디")
    UUID profileId,
    @Schema(description = "유저 온라인 여부")
    Boolean online
) {

}
