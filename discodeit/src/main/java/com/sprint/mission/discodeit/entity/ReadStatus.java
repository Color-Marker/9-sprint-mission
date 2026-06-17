package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "read_statuses", uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_channel", columnNames = {"user_id", "channel_id"})
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY) // 3. 1:N 관계에서 N쪽이므로 ManyToOne
  @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
  private User user;
  @ManyToOne(fetch = FetchType.LAZY) // 3. 1:N 관계에서 N쪽이므로 ManyToOne
  @JoinColumn(name = "channel_id", nullable = false, columnDefinition = "uuid")
  private Channel channel;
  @Column(nullable = false)
  private Instant lastReadAt;
  @Column(nullable = false)
  private boolean notificationEnabled;

  public void update(Instant newLastReadAt, boolean notificationEnabled) {
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
    }
    if (notificationEnabled) {
      this.notificationEnabled = !this.notificationEnabled;
    }
  }
}
