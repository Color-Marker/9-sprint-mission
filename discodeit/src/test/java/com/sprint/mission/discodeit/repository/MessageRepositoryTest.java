package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.lang.reflect.Field;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private TestEntityManager em;

  @Test
  @DisplayName("커서 기반 메시지 조회 - 성공 (커서 이전 데이터만 조회)")
  void findAllByCursorSuccess() throws Exception {
    User author = User.builder().username("test").email("t@t.com").password("p").build();
    em.persist(author);
    Channel channel = Channel.builder().name("test").type(ChannelType.PUBLIC).build();
    em.persist(channel);

    Instant t1 = Instant.parse("2026-03-31T10:00:00Z");
    Instant cursor = t1.plusSeconds(1);
    Instant t3 = cursor.plusSeconds(1);

    Message oldMsg = Message.builder().content("old").channel(channel).author(author).build();
    em.persist(oldMsg);

    Message newMsg = Message.builder().content("new").channel(channel).author(author).build();
    em.persist(newMsg);

    em.flush();

    em.getEntityManager().createNativeQuery(
            "UPDATE messages SET created_at = ? WHERE id = ?")
        .setParameter(1, t1)
        .setParameter(2, oldMsg.getId())
        .executeUpdate();

    em.getEntityManager().createNativeQuery(
            "UPDATE messages SET created_at = ? WHERE id = ?")
        .setParameter(1, t3)
        .setParameter(2, newMsg.getId())
        .executeUpdate();

    em.clear();

    Pageable pageable = PageRequest.of(0, 10);
    Slice<Message> result = messageRepository.findAllByCursor(channel.getId(), cursor, pageable);

    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).getContent()).isEqualTo("old");
  }

  @Test
  @DisplayName("채널 ID로 모든 메시지 삭제 - 성공")
  void deleteAllByChannelIdSuccess() {
    User author = User.builder()
        .username("test")
        .email("test@test.com")
        .password("test")
        .build();
    em.persist(author);
    Channel channel = Channel.builder().name("test").type(ChannelType.PUBLIC).build();
    em.persist(channel);
    em.persist(Message.builder().content("1").channel(channel).author(author).build());
    em.persist(Message.builder().content("2").channel(channel).author(author).build());
    em.flush();

    messageRepository.deleteAllByChannelId(channel.getId());
    em.flush();
    em.clear();

    assertThat(messageRepository.findAll()).isEmpty();
  }

}
