package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
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
public class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private TestEntityManager em;

  @Test
  @DisplayName("공개 채널 또는 구독 채널 조회 - 성공")
  void findPublicOrSubscribedChannelsSuccess() {
    Channel publicChannel = Channel.builder().name("Pub").type(ChannelType.PUBLIC).build();
    Channel privateChannel = Channel.builder().name("Priv").type(ChannelType.PRIVATE).build();
    em.persist(publicChannel);
    em.persist(privateChannel);
    List<Channel> result = channelRepository.findPublicOrSubscribedChannels(
        List.of(privateChannel.getId()));
    assertThat(result).hasSize(2);
  }
}
