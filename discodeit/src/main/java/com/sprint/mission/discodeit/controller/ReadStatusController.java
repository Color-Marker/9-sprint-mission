package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Read Status API")
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "읽음 상태 생성")
  @PostMapping("")
  public ResponseEntity<?> create(
      @Parameter(description = "읽음 상태 정보")
      @RequestBody ReadStatusCreateRequest dto) {
    ReadStatusDto readStatus = readStatusService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
  }

  @Operation(summary = "읽음 상태 수정")
  @PatchMapping("/{statusId}")
  public ResponseEntity<?> edit(
      @Parameter(description = "읽음 상태 ID")
      @PathVariable UUID statusId,
      @Parameter(description = "읽음 상태 정보")
      @RequestBody ReadStatusUpdateRequest dto
  ) {
    ReadStatusDto readStatus = readStatusService.update(statusId, dto);
    return ResponseEntity.ok(readStatus);
  }

  @Operation(summary = "유저별 읽음 상태 출력")
  @GetMapping("")
  public ResponseEntity<?> statusList(
      @Parameter(description = "유저 ID")
      @RequestParam UUID userId
  ) {
    List<ReadStatusDto> statusList = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(statusList);
  }
}
