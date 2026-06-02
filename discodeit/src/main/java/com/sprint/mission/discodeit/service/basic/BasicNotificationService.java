package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final ReadStatusRepository readStatusRepository;
  private final NotificationRepository notificationRepository;

  @Override
  public void createByMessage(Message message) {
    String title = message.getAuthor().getUsername() + " (#" + message.getChannel().getName() + ")";
    String content = message.getContent();
    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannel(message.getChannel());
    List<ReadStatus> targets = readStatuses.stream().filter(ReadStatus::isNotificationEnabled)
        .toList();
    List<User> users = targets.stream().map(ReadStatus::getUser).toList();
    for (User u : users) {
      if (u.getId().equals(message.getAuthor().getId())) {
        continue;
      }
      Notification notification = new Notification(u.getId(), title, content);
      notificationRepository.save(notification);
    }
    log.info("알림 생성 완료");
  }

  @Override
  public void createByRole(User user, Role pastRole, Role newRole) {
    String title = "권한이 변경되었습니다.";
    String content = pastRole.toString() + " -> " + newRole.toString();
    Notification notification = new Notification(user.getId(), title, content);
    notificationRepository.save(notification);
  }

  @Override
  public List<NotificationDto> get(UUID userId) {
    List<Notification> notifications = notificationRepository.findAllByReceiverId(userId);
    List<NotificationDto> dtos = notifications.stream()
        .map(n -> {
          NotificationDto dto = new NotificationDto(
              n.getId(),
              n.getCreatedAt(),
              n.getReceiverId(),
              n.getTitle(),
              n.getContent()
          );
          return dto;
        })
        .toList();
    return dtos;
  }

  @Override
  public void delete(UUID notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new NotificationNotFoundException(notificationId));
    notificationRepository.delete(notification);
  }
}
