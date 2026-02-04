package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<UUID> findUserIdByChannelId(UUID channelId);
    List<UUID> findChannelIdByUserId(UUID userId);
    Optional<ReadStatus> findByChannelAndUser(UUID channelId, UUID userId);
    List<ReadStatus> findAll();
    Boolean existsById(UUID id);
    void deleteById(UUID id);
}
