package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.ChannelType;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    @Schema(description = "채널 아이디")
    UUID id,
    @Schema(description = "채널 타입")
    ChannelType type,
    @Schema(description = "채널 이름")
    String name,
    @Schema(description = "채널 설명")
    String description,
    @Schema(description = "채널 참가자 ID 목록")
    List<UUID> participantIds,
    @Schema(description = "가장 최근 메시지 일자")
    Instant lastMessageAt
) {

}