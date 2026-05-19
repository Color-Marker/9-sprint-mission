package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.DiscodeitUserDetails;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("securityService")
@RequiredArgsConstructor
public class SecurityService {

  private final MessageRepository messageRepository;

  public boolean isMessageOwner(UUID messageId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    DiscodeitUserDetails user = (DiscodeitUserDetails) auth.getPrincipal();

    return messageRepository.findById(messageId)
        .map(message -> message.getAuthor().getId().equals(user.getUserDto().id()))
        .orElse(false);
  }
}
