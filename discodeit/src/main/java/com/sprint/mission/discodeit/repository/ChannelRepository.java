package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  @Query("Select c FROM Channel c "
      + "WHERE c.type = 'PUBLIC' "
      + "OR c.id IN :subscribedIds")
  List<Channel> findPublicOrSubscribedChannels(@Param("subscribedIds") List<UUID> subscribedIds);
  
  List<Channel> findByType(ChannelType type);
}
