package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserLoginReqDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password
            ){
        UserLoginReqDto dto = new UserLoginReqDto(username, password);
        User user = authService.login(dto);
        return ResponseEntity.ok(user);
    }
}
