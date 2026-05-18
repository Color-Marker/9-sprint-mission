package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@Tag(name = "User API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(summary = "유저 생성")
  @PostMapping(
      path = "",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<UserDto> create(
      @Parameter(description = "유저 정보")
      @Valid @RequestPart("userCreateRequest") UserCreateRequest UserCreateRequest,
      @Parameter(description = "유저 프로필 파일")
      @Valid @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> binaryDto = Optional.ofNullable(profile)
        .filter(file -> !file.isEmpty())
        .map(file ->
            {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getBytes(),
                    file.getContentType()
                );
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            }
        );
    UserDto user = userService.create(UserCreateRequest, binaryDto);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(user.id())
        .toUri();

    return ResponseEntity.created(location).body(user);
  }

  @Operation(summary = "유저 수정")
  @PatchMapping(
      path = "/{userId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<UserDto> edit(
      @Parameter(description = "유저 ID")
      @Valid @PathVariable UUID userId,
      @Parameter(description = "유저 정보")
      @Valid @RequestPart("userUpdateRequest") UserUpdateRequest userDto,
      @Parameter(description = "유저 프로필 파일")
      @Valid @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> binaryDto = Optional.ofNullable(profile)
        .filter(file -> !file.isEmpty())
        .map(file ->
            {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getBytes(),
                    file.getContentType()
                );
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            }
        );
    UserDto result = userService.update(userId, userDto, binaryDto);
    return ResponseEntity.ok(result);
  }

  @Operation(summary = "유저 삭제")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "유저 ID")
      @Valid @PathVariable UUID userId
  ) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "전체 유저 출력")
  @GetMapping("")
  public ResponseEntity<List<UserDto>> userList() {
    List<UserDto> allUsers = userService.findAll();
    return ResponseEntity.ok(allUsers);
  }
  
}
