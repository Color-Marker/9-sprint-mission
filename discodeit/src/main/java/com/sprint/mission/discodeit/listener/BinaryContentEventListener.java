package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class BinaryContentEventListener {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentService binaryContentService;

  @TransactionalEventListener(
      phase = TransactionPhase.AFTER_COMMIT
  )
  public void on(
      BinaryContentCreatedEvent event
  ) {
    try {
      binaryContentStorage.put(event.contentId(), event.bytes());
      binaryContentService.updateStatus(event.contentId(), BinaryContentStatus.SUCCESS);
    } catch (Exception e) {
      log.error("바이너리 저장 실패 - contentId: {}", event.contentId(), e);
      binaryContentService.updateStatus(event.contentId(), BinaryContentStatus.FAIL);
    }
  }


}
