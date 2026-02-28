package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.PrivateChannelCreateReqDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateReqDto;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateReqDto;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/public/create")
    public ResponseEntity<?> publicCreate(@RequestBody PublicChannelCreateReqDto dto){
        Channel channel = channelService.create(dto);
        ChannelDto result = channelService.find(channel.getId());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/private/create")
    public ResponseEntity<?> privateCreate(@RequestBody PrivateChannelCreateReqDto dto){
        Channel channel = channelService.create(dto);
        ChannelDto result = channelService.find(channel.getId());
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/public/edit/{channelId}")
    public ResponseEntity<?> edit(
            @PathVariable UUID channelId,
            @RequestBody PublicChannelUpdateReqDto dto
    ){
        channelService.update(channelId, dto);
        ChannelDto result = channelService.find(channelId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<?> delete(
            @PathVariable UUID channelId
    ){
        channelService.delete(channelId);
        return ResponseEntity.ok("channel: " + channelId + "가 삭제되었습니다.");

    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<?> channelList(
            @PathVariable UUID userId
    ){
        List<ChannelDto> channelList = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channelList);
    }


}
