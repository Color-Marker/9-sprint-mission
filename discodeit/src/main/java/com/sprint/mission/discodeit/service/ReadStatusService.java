package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateReqDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateReqDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateReqDto request);
    ReadStatus find(UUID readStatusId);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(UUID readStatusId, ReadStatusUpdateReqDto request);
    void delete(UUID readStatusId);
}
