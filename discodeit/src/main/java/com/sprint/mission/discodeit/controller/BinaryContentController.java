package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/")
  public ResponseEntity<?> findSome(
      @RequestParam List<UUID> fileIdList
  ) {
    List<BinaryContent> fileList = binaryContentService.findAllByIdIn(fileIdList);
    return ResponseEntity.ok(ResponseDto.ok(fileList));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> find(
      @PathVariable UUID id
  ) {
    BinaryContent file = binaryContentService.find(id);
    return ResponseEntity.ok(ResponseDto.ok(file));
  }
}
