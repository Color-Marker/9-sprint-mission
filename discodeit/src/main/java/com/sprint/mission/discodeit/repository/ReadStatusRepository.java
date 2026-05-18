package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllByUserId(UUID userId);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  List<ReadStatus> findAllByChannel(Channel channel);

  void deleteAllByChannelId(UUID channelId);

  @EntityGraph(attributePaths = {
      "channel"
  })
  Optional<ReadStatus> findWithExtraById(UUID readStatusId);

  @EntityGraph(attributePaths = {
      "channel"
  })
  List<ReadStatus> findAllWithExtraByUserId(UUID userId);
}
