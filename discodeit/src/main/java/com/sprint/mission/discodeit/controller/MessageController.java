package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateReqDto;
import com.sprint.mission.discodeit.dto.MessageCreateReqDto;
import com.sprint.mission.discodeit.dto.MessageUpdateReqDto;
import com.sprint.mission.discodeit.dto.UserCreateReqDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping(
            path = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> create(
            @RequestPart("messageCreateReqDto") MessageCreateReqDto messageCreateReqDto,
            @RequestPart("files") List<MultipartFile> files
    ){
        List<BinaryContentCreateReqDto> binaryDtos = new ArrayList<>();
        try {
            for(MultipartFile file: files){
                BinaryContentCreateReqDto data = new BinaryContentCreateReqDto(file.getName(), file.getContentType(), file.getBytes());
                binaryDtos.add(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽을 수 없습니다.");
        }
        Message message = messageService.create(messageCreateReqDto, binaryDtos);
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/edit/{messageId}")
    public ResponseEntity<?> edit(
            @PathVariable UUID messageId,
            @RequestBody MessageUpdateReqDto dto
    ){
        Message message = messageService.update(messageId,dto);
        return  ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<?> delete(
            @PathVariable UUID messageId
    ){
        messageService.delete(messageId);
        return ResponseEntity.ok("message: " + messageId + "가 삭제되었습니다.");
    }

    @GetMapping("/list/{channelId}")
    public ResponseEntity<?> messageList(
            @PathVariable UUID channelId
    ){
        List<Message> messages = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }
}
