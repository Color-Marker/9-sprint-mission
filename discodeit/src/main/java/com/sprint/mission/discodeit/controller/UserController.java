package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateReqDto;
import com.sprint.mission.discodeit.dto.UserCreateReqDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateReqDto;
import com.sprint.mission.discodeit.dto.UserUpdateReqDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(
            path = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> create(
            @RequestPart("userCreateReqDto") UserCreateReqDto userCreateReqDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ){
        Optional<BinaryContentCreateReqDto> binaryDto = Optional.ofNullable(profile)
                .filter(file -> !file.isEmpty())
                .map(file -> {
                    try {
                        return new BinaryContentCreateReqDto(
                                file.getOriginalFilename(),
                                file.getContentType(),
                                file.getBytes()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException("프로필 파일을 읽을 수 없습니다.", e);
                    }
                });
        User user = userService.create(userCreateReqDto, binaryDto);
        UserDto result = userService.find(user.getId());
        return ResponseEntity.ok(result);
    }

    @PatchMapping(
            path = "/edit/{userId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> edit(
            @PathVariable UUID userId,
            @RequestPart("userUpdateReqDto") UserUpdateReqDto userDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ){
        Optional<BinaryContentCreateReqDto> binaryDto = Optional.ofNullable(profile)
                .filter(file -> !file.isEmpty())
                .map(file -> {
                    try {
                        return new BinaryContentCreateReqDto(
                                file.getOriginalFilename(),
                                file.getContentType(),
                                file.getBytes()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException("프로필 파일을 읽을 수 없습니다.", e);
                    }
                });
        userService.update(userId, userDto, binaryDto);
        UserDto result = userService.find(userId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> delete(
            @PathVariable UUID userId
    ){
        userService.delete(userId);
        return ResponseEntity.ok("user: " + userId + "가 삭제되었습니다.");
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> userList(){
        List<UserDto> allUsers = userService.findAll();
        return ResponseEntity.ok(allUsers);
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<?> update(
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdateReqDto dto
    ){
        UserStatus userStatus = userStatusService.updateByUserId(userId, dto);
        return ResponseEntity.ok(userStatus);
    }
}
