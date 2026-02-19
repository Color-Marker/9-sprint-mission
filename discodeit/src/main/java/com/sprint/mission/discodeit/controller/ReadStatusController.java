package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateReqDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateReqDto;
import com.sprint.mission.discodeit.dto.ResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping("")
  public ResponseEntity<?> create(@RequestBody ReadStatusCreateReqDto dto) {
    ReadStatus readStatus = readStatusService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.ok(readStatus));
  }

  @PatchMapping("/{statusId}")
  public ResponseEntity<?> edit(
      @PathVariable UUID statusId,
      @RequestBody ReadStatusUpdateReqDto dto
  ) {
    ReadStatus readStatus = readStatusService.update(statusId, dto);
    return ResponseEntity.ok(ResponseDto.ok(readStatus));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> statusList(
      @PathVariable UUID userId
  ) {
    List<ReadStatus> statusList = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(ResponseDto.ok(statusList));
  }
}
