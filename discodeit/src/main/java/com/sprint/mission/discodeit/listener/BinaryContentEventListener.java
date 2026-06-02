package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class BinaryContentEventListener {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentService binaryContentService;

  private final NotificationService notificationService;

  @Async("eventExecutor")
  @Retryable(
      retryFor = RuntimeException.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000)
  )
  @TransactionalEventListener(
      phase = TransactionPhase.AFTER_COMMIT
  )
  public void on(
      BinaryContentCreatedEvent event
  ) {
    binaryContentStorage.put(event.contentId(), event.bytes());
    binaryContentService.updateStatus(event.contentId(), BinaryContentStatus.SUCCESS);
  }

  @Recover
  public void recover(RuntimeException ex, BinaryContentCreatedEvent event) {
    String requestId = MDC.get("requestId");
    Throwable cause = ex;
    while (cause.getCause() != null) {
      cause = cause.getCause();
    }

    String message = String.format(
        "RequestId: %s\nBinaryContentId: %s\nError: %s",
        requestId,
        event.contentId(),
        cause.getMessage()
    );
    log.error("바이너리 저장 최종 실패\n{}", message);
    binaryContentService.updateStatus(event.contentId(), BinaryContentStatus.FAIL);
    notificationService.createByError(message);
  }


}
