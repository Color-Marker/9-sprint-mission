package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationRequiredEventListener {

  private final NotificationService notificationService;

  @TransactionalEventListener
  public void on(
      MessageCreatedEvent event
  ) {
    notificationService.createByMessage(event.message());
  }

  @TransactionalEventListener
  public void on(
      RoleUpdatedEvent event
  ) {
    notificationService.createByRole(event.user(), event.pastRole(), event.newRole());
  }
}
