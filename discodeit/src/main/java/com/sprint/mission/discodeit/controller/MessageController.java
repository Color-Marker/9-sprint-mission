package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateReqDto;
import com.sprint.mission.discodeit.dto.MessageCreateReqDto;
import com.sprint.mission.discodeit.dto.MessageUpdateReqDto;
import com.sprint.mission.discodeit.dto.ResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag(name = "Message API")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @Operation(summary = "메시지 생성")
  @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> create(
      @Parameter(description = "메시지 정보")
      @RequestPart("messageCreateReqDto") MessageCreateReqDto messageCreateReqDto,
      @Parameter(description = "파일 정보")
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    List<BinaryContentCreateReqDto> binaryDtos = new ArrayList<>();
    if (files != null) {
      try {
        for (MultipartFile file : files) {
          BinaryContentCreateReqDto data = new BinaryContentCreateReqDto(file.getName(),
              file.getContentType(), file.getBytes());
          binaryDtos.add(data);
        }
      } catch (IOException e) {
        throw new RuntimeException("파일을 읽을 수 없습니다.");
      }
    }
    Message message = messageService.create(messageCreateReqDto, binaryDtos);
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.ok(message));
  }

  @Operation(summary = "메시지 수정")
  @PatchMapping("/{messageId}")
  public ResponseEntity<?> edit(
      @Parameter(description = "메시지 ID")
      @PathVariable UUID messageId,
      @Parameter(description = "메시지 정보")
      @RequestBody MessageUpdateReqDto dto) {
    Message message = messageService.update(messageId, dto);
    return ResponseEntity.ok(ResponseDto.ok(message));
  }

  @Operation(summary = "메시지 삭제")
  @DeleteMapping("/{messageId}")
  public ResponseEntity<?> delete(
      @Parameter(description = "메시지 ID")
      @PathVariable UUID messageId) {
    messageService.delete(messageId);
    String result = "message: " + messageId + "가 삭제되었습니다.";
    return ResponseEntity.ok(ResponseDto.ok(result));
  }

  @Operation(summary = "채널 별 메시지 출력")
  @GetMapping("/{channelId}")
  public ResponseEntity<?> messageList(
      @Parameter(description = "채널 ID")
      @PathVariable UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(ResponseDto.ok(messages));
  }
}
