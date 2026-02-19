package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateReqDto(
    @Schema(description = "참가자 목록")
    List<UUID> participantIds,
    @Schema(description = "채널 이름")
    String name,
    @Schema(description = "채널 정보")
    String description
) {

}
