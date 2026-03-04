package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Binary Content API")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @Operation(summary = "다중 파일 출력")
  @GetMapping("")
  public ResponseEntity<?> findSome(
      @Parameter(description = "다중 파일 ID 정보")
      @RequestParam List<UUID> binaryContentId
  ) {
    List<BinaryContent> fileList = binaryContentService.findAllByIdIn(binaryContentId);
    return ResponseEntity.ok(fileList);
  }

  @Operation(summary = "단일 파일 출력")
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<?> find(
      @Parameter(description = "파일 ID")
      @PathVariable UUID binaryContentId
  ) {
    BinaryContent file = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(file);
  }
}
