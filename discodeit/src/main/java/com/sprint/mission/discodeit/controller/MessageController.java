package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@Tag(name = "Message API")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @Operation(summary = "메시지 생성")
  @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> create(
      @Parameter(description = "메시지 정보")
      @Valid @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @Parameter(description = "파일 정보")
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

    log.info("메시지 생성 요청 - 메시지 정보: {}, 파일 정보: {}", messageCreateRequest, attachments);

    List<BinaryContentCreateRequest> binaryDtos = new ArrayList<>();
    if (attachments != null) {
      try {
        for (MultipartFile file : attachments) {
          if (file.isEmpty()) {
            continue;
          }
          log.info("파일 업로드 요청 - 파일명: {}, 타입: {}, 크기: {} bytes",
              file.getOriginalFilename(), file.getContentType(), file.getSize());
          BinaryContentCreateRequest data = new BinaryContentCreateRequest(
              file.getOriginalFilename(),
              file.getBytes(), file.getContentType());
          binaryDtos.add(data);
        }
      } catch (IOException e) {
        log.warn("파일 인식 실패 예외 발생");
        throw new RuntimeException("파일을 읽을 수 없습니다.");
      }
    }
    MessageDto message = messageService.create(messageCreateRequest, binaryDtos);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(message.id())
        .toUri();
    return ResponseEntity.created(location).body(message);
  }

  @Operation(summary = "메시지 수정")
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> edit(
      @Parameter(description = "메시지 ID")
      @PathVariable UUID messageId,
      @Parameter(description = "메시지 정보")
      @RequestBody MessageUpdateRequest dto) {

    log.info("메시지 수정 요청 - 메시지 ID: {}, 메시지 정보: {}", messageId, dto);

    MessageDto message = messageService.update(messageId, dto);
    return ResponseEntity.ok(message);
  }

  @Operation(summary = "메시지 삭제")
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "메시지 ID")
      @PathVariable UUID messageId) {

    log.info("메시지 삭제 요청 - 메시지 ID: {}", messageId);

    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "채널 별 메시지 출력")
  @GetMapping("")
  public ResponseEntity<PageResponse<MessageDto>> messageList(
      @Parameter(description = "채널 ID")
      @Valid @RequestParam UUID channelId,
      @RequestParam(required = false) Instant cursor,
      @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor,
        pageable);
    return ResponseEntity.ok(messages);
  }
}
