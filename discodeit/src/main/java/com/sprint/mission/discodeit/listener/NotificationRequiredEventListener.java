package com.sprint.mission.discodeit.listener;


import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.event.S3UploadFailedEvent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class NotificationRequiredEventListener {
//
//  private final NotificationService notificationService;
//  private final BinaryContentService binaryContentService;
//
//  @Async("eventExecutor")
//  @TransactionalEventListener
//  public void on(
//      MessageCreatedEvent event
//  ) {
//    notificationService.createByMessage(event.message());
//  }
//
//  @Async("eventExecutor")
//  @TransactionalEventListener
//  public void on(
//      RoleUpdatedEvent event
//  ) {
//    notificationService.createByRole(event.user(), event.pastRole(), event.newRole());
//  }
//
//  @Async("eventTaskExecutor")
//  @EventListener
//  public void on(S3UploadFailedEvent event) {
//    String message = String.format(
//        "RequestId: %s\nBinaryContentId: %s\nError: %s",
//        event.requestId(),
//        event.contentId(),
//        event.errorMessage()
//    );
//    log.error("바이너리 저장 최종 실패\n{}", message);
//    binaryContentService.updateStatus(event.contentId(), BinaryContentStatus.FAIL);
//    notificationService.createByError(message);
//  }
//}
