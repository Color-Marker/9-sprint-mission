package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  @EntityGraph(attributePaths = {
      "profile",
      "status"
  })
  List<User> findAllWithProfileAndStatusBy();

  @EntityGraph(attributePaths = {
      "profile",
      "status"
  })
  Optional<User> findWithProfileAndStatusById(UUID userId);

  boolean existsByRole(Role role);
}