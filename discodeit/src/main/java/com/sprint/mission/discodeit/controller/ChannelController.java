package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.PrivateChannelCreateReqDto;
import com.sprint.mission.discodeit.dto.PublicChannelReqDto;
import com.sprint.mission.discodeit.dto.ResponseDto;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/public")
  public ResponseEntity<?> publicCreate(@RequestBody PublicChannelReqDto dto) {
    Channel channel = channelService.create(dto);
    ChannelDto result = channelService.find(channel.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.ok(result));
  }

  @PostMapping("/private")
  public ResponseEntity<?> privateCreate(@RequestBody PrivateChannelCreateReqDto dto) {
    Channel channel = channelService.create(dto);
    ChannelDto result = channelService.find(channel.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.ok(result));
  }

  @PutMapping("/public/{channelId}")
  public ResponseEntity<?> edit(
      @PathVariable UUID channelId,
      @RequestBody PublicChannelReqDto dto
  ) {
    channelService.update(channelId, dto);
    ChannelDto result = channelService.find(channelId);
    return ResponseEntity.ok(ResponseDto.ok(result));
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<?> delete(
      @PathVariable UUID channelId
  ) {
    channelService.delete(channelId);
    String result = "channel: " + channelId + "가 삭제되었습니다.";
    return ResponseEntity.ok(ResponseDto.ok(result));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> channelList(
      @PathVariable UUID userId
  ) {
    List<ChannelDto> channelList = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(ResponseDto.ok(channelList));
  }
}
