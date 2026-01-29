package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelFindResDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.PrivateChannelDto;
import com.sprint.mission.discodeit.dto.PublicChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublicChannel(PublicChannelDto publicChannelDto);
    Channel createPrivateChannel(PrivateChannelDto privateChannelDto);
    ChannelFindResDto find(UUID channelId);
    List<ChannelFindResDto> findAllByUserId(UUID userId);
    Channel update(ChannelUpdateDto channelUpdateDto);
    void delete(UUID channelId);
}
