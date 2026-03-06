package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

  Optional<Message> findFirstByChannelOrderByCreatedAtDesc(Channel channel);

  void deleteAllByChannelId(UUID channelId);
}
