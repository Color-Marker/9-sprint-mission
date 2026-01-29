package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContentCreateDto binaryContentCreateDto);
    Optional<BinaryContent> findById(UUID id);
    List<BinaryContent> findAll();
    Boolean existsById(UUID id);
    void deleteById(UUID id);
}
