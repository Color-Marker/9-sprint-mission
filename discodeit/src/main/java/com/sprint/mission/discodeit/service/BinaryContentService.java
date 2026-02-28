package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateReqDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContentCreateReqDto dto);
    BinaryContent find(UUID binaryContentId);
    List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);
    void delete(UUID binaryContentId);
}
