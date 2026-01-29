package com.sprint.mission.discodeit.dto;

public record BinaryContentCreateDto(
        String fileName,
        String contentType,
        Long size
) {
}
