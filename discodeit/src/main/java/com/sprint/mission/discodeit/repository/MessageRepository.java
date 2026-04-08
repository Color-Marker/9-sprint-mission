package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import jakarta.persistence.Entity;
import java.awt.PageAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

  @Query("SELECT m FROM Message m WHERE m.channel.id = :channelId "
      + "AND m.createdAt < :cursor "
      + "ORDER BY m.createdAt DESC")
  Slice<Message> findAllByCursor(@Param("channelId") UUID channelId,
      @Param("cursor") Instant cursor, Pageable pageable);
}
