package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

  Optional<UserStatus> findByUserId(UUID userId);

  boolean existsByUserId(UUID userId);

  void deleteByUserId(UUID userId);

  @EntityGraph(attributePaths = {"user"})
  Optional<UserStatus> findWithUserById(UUID id);

  @EntityGraph(attributePaths = {"user"})
  Optional<UserStatus> findWithUserByUserId(UUID userId);

  @EntityGraph(attributePaths = {"user"})
  List<UserStatus> findAllWithUserBy();
}
