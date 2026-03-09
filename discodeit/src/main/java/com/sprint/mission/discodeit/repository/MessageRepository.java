package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import jakarta.persistence.Entity;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Optional<Message> findFirstByChannelOrderByCreatedAtDesc(Channel channel);

  void deleteAllByChannelId(UUID channelId);

  @EntityGraph(attributePaths = {
      "channel",
      "author.status"
  })
  Optional<Message> findWithChannelAuthorAttachmentById(UUID messageId);

  @EntityGraph(attributePaths = {
      "channel",
      "author.status"
  })
  Slice<Message> findAllWithExtraByChannelId(UUID channelId, Pageable pageable);

}
