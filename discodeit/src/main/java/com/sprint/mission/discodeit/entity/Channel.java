package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static java.time.Instant.now;

@Entity
@Table(name = "channels")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ChannelType type;
  @Column(length = 100)
  private String name;
  @Column(length = 500)
  private String description;

  public void update(String newName, String newDescription) {
    ;
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
    }
  }
}
