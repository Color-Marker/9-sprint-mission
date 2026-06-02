package com.sprint.mission.discodeit.listener;


import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationRequiredEventListener {

  private final NotificationService notificationService;

  @Async("eventExecutor")
  @TransactionalEventListener
  public void on(
      MessageCreatedEvent event
  ) {
    notificationService.createByMessage(event.message());
  }

  @Async("eventExecutor")
  @TransactionalEventListener
  public void on(
      RoleUpdatedEvent event
  ) {
    notificationService.createByRole(event.user(), event.pastRole(), event.newRole());
  }
}
