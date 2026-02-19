package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ResponseDto;
import com.sprint.mission.discodeit.dto.UserLoginReqDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "로그인")
  @PostMapping("/login")
  public ResponseEntity<?> login(
      @Parameter(description = "로그인 정보")
      @RequestBody UserLoginReqDto dto
  ) {
    User user = authService.login(dto);
    return ResponseEntity.ok(ResponseDto.ok(user));
  }
}
