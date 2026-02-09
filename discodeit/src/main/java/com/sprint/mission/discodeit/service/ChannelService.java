package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.PrivateChannelCreateReqDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateReqDto;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateReqDto;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(PublicChannelCreateReqDto dto);
    Channel create(PrivateChannelCreateReqDto dto);
    ChannelDto find(UUID channelId);
    List<ChannelDto> findAllByUserId(UUID userId);
    Channel update(UUID channelId, PublicChannelUpdateReqDto dto);
    void delete(UUID channelId);}
