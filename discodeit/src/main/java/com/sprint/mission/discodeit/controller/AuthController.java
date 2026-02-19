package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ResponseDto;
import com.sprint.mission.discodeit.dto.UserLoginReqDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestBody UserLoginReqDto dto
  ) {
    User user = authService.login(dto);
    return ResponseEntity.ok(ResponseDto.ok(user));
  }
}
