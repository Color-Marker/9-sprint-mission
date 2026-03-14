package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.PrivateChannelCreateReqDto;
import com.sprint.mission.discodeit.dto.PublicChannelReqDto;
import com.sprint.mission.discodeit.dto.ResponseDto;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel API")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @Operation(summary = "public 채널 생성")
  @PostMapping("/public")
  public ResponseEntity<?> publicCreate(
      @Parameter(description = "채널 정보")
      @RequestBody PublicChannelReqDto dto) {
    Channel channel = channelService.create(dto);
    ChannelDto result = channelService.find(channel.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @Operation(summary = "private 채널 생성")
  @PostMapping("/private")
  public ResponseEntity<?> privateCreate(
      @Parameter(description = "채널 정보")
      @RequestBody PrivateChannelCreateReqDto dto) {
    Channel channel = channelService.create(dto);
    ChannelDto result = channelService.find(channel.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @Operation(summary = "public 채널 수정")
  @PutMapping("/public/{channelId}")
  public ResponseEntity<?> edit(
      @Parameter(description = "채널 ID")
      @PathVariable UUID channelId,
      @Parameter(description = "채널 정보")
      @RequestBody PublicChannelReqDto dto
  ) {
    channelService.update(channelId, dto);
    ChannelDto result = channelService.find(channelId);
    return ResponseEntity.ok(result);
  }

  @Operation(summary = "채널 삭제")
  @DeleteMapping("/{channelId}")
  public ResponseEntity<?> delete(
      @Parameter(description = "채널 ID")
      @PathVariable UUID channelId
  ) {
    channelService.delete(channelId);
    String result = "channel: " + channelId + "가 삭제되었습니다.";
    return ResponseEntity.ok(result);
  }

  @Operation(summary = "유저 소속 채널 출력")
  @GetMapping("")
  public ResponseEntity<?> channelList(
      @Parameter(description = "유저 ID")
      @RequestParam UUID userId
  ) {
    List<ChannelDto> channelList = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channelList);
  }
}
