package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.BinaryContentCreateReqDto;
import com.sprint.mission.discodeit.dto.MessageCreateReqDto;
import com.sprint.mission.discodeit.dto.MessageUpdateReqDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateReqDto messageCreateRequest, List<BinaryContentCreateReqDto> binaryContentCreateRequests);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(UUID messageId, MessageUpdateReqDto request);
    void delete(UUID messageId);
}
