package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "User API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @Operation(summary = "유저 생성")
  @PostMapping(
      path = "",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<?> create(
      @Parameter(description = "유저 정보")
      @RequestPart("userCreateRequest") UserCreateRequest UserCreateRequest,
      @Parameter(description = "유저 프로필 파일")
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> binaryDto = Optional.ofNullable(profile)
        .filter(file -> !file.isEmpty())
        .map(file -> {
          try {
            return new BinaryContentCreateRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
            );
          } catch (IOException e) {
            throw new RuntimeException("프로필 파일을 읽을 수 없습니다.", e);
          }
        });
    User user = userService.create(UserCreateRequest, binaryDto);
    UserDto result = userService.find(user.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @Operation(summary = "유저 수정")
  @PatchMapping(
      path = "/{userId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<?> edit(
      @Parameter(description = "유저 ID")
      @PathVariable UUID userId,
      @Parameter(description = "유저 정보")
      @RequestPart("userUpdateRequest") UserUpdateRequest userDto,
      @Parameter(description = "유저 프로필 파일")
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> binaryDto = Optional.ofNullable(profile)
        .filter(file -> !file.isEmpty())
        .map(file -> {
          try {
            return new BinaryContentCreateRequest(
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

  @Operation(summary = "유저 삭제")
  @DeleteMapping("/{userId}")
  public ResponseEntity<?> delete(
      @Parameter(description = "유저 ID")
      @PathVariable UUID userId
  ) {
    userService.delete(userId);
    String result = "user: " + userId + "가 삭제되었습니다.";
    return ResponseEntity.ok(result);
  }

  @Operation(summary = "전체 유저 출력")
  @GetMapping("")
  public ResponseEntity<?> userList() {
    List<UserDto> allUsers = userService.findAll();
    return ResponseEntity.ok(allUsers);
  }

  @Operation(summary = "유저 읽음 상태 업데이트")
  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<?> update(
      @Parameter(description = "유저 ID")
      @PathVariable UUID userId
  ) {
    UserStatus userStatus = userStatusService.updateByUserId(userId);
    return ResponseEntity.ok(userStatus);
  }
}
