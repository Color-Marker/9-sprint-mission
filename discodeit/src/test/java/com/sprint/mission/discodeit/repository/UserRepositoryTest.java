package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private TestEntityManager em;

  @Test
  @DisplayName("ID 이용 프로필/상태 조회 성공")
  void findWithProfileAndStatusByIdSuccess() {
    User user = User.builder().username("test").email("test@test.com").password("test").build();
    em.persist(user);
    em.flush();
    em.clear();
    Optional<User> result = userRepository.findWithProfileAndStatusById(user.getId());
    assertThat(result).isPresent();
    assertThat(result.get().getUsername()).isEqualTo("test");
  }

  @Test
  @DisplayName("중복 이메일 체크 - 검색 실패")
  void existsByEmailFalse() {
    boolean exists = userRepository.existsByEmail("non-exist@t.com");
    assertThat(exists).isFalse();
  }

}
