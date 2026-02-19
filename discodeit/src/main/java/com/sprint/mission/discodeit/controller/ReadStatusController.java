package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateReqDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateReqDto;
import com.sprint.mission.discodeit.dto.ResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
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
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "읽음 상태 생성")
  @PostMapping("")
  public ResponseEntity<?> create(
      @Parameter(description = "읽음 상태 정보")
      @RequestBody ReadStatusCreateReqDto dto) {
    ReadStatus readStatus = readStatusService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.ok(readStatus));
  }

  @Operation(summary = "읽음 상태 수정")
  @PatchMapping("/{statusId}")
  public ResponseEntity<?> edit(
      @Parameter(description = "읽음 상태 ID")
      @PathVariable UUID statusId,
      @Parameter(description = "읽음 상태 정보")
      @RequestBody ReadStatusUpdateReqDto dto
  ) {
    ReadStatus readStatus = readStatusService.update(statusId, dto);
    return ResponseEntity.ok(ResponseDto.ok(readStatus));
  }

  @Operation(summary = "유저별 읽음 상태 출력")
  @GetMapping("/{userId}")
  public ResponseEntity<?> statusList(
      @Parameter(description = "유저 ID")
      @PathVariable UUID userId
  ) {
    List<ReadStatus> statusList = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(ResponseDto.ok(statusList));
  }
}
