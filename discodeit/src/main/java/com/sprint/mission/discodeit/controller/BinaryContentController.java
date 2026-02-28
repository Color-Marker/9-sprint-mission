package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/findAll")
    public ResponseEntity<?> viewFiles(
            @RequestParam List<UUID> fileIdList
    ){
        List<BinaryContent> fileList = binaryContentService.findAllByIdIn(fileIdList);
        return ResponseEntity.ok(fileList);
    }

    @GetMapping("/find")
    public ResponseEntity<BinaryContent> viewFile(
        @RequestParam UUID binaryContentId
    ){
        BinaryContent file = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(file);
    }
}
