package com.sprint.mission.discodeit.dto;

public record BinaryContentCreateReqDto(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
