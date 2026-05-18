package com.sprint.mission.discodeit.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.handler.GlobalExceptionHandler;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;
  @MockitoBean
  UserService userService;
  @MockitoBean
  UserStatusService userStatusService;

  @Test
  @DisplayName("유저 생성 - 성공")
  void createUser_success() throws Exception {
    UUID userId = UUID.randomUUID();
    UserDto response = new UserDto(userId, "guest", "guest@codeit.com", null, false);

    given(userService.create(any(), any())).willReturn(response);

    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
        """
            {"username":"guest","email":"guest@codeit.com","password":"password123"}
            """.getBytes()
    );

    mockMvc.perform(multipart("/api/users").file(userPart))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("guest"))
        .andExpect(jsonPath("$.email").value("guest@codeit.com"));
  }

  @Test
  @DisplayName("유저 생성 - 실패 (validation: username 누락)")
  void createUser_fail_validation() throws Exception {
    MockMultipartFile userPart = new MockMultipartFile(
        "userCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
        """
            {"username":"","email":"guest@codeit.com","password":"password123"}
            """.getBytes()
    );

    mockMvc.perform(multipart("/api/users").file(userPart))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
  }

  @Test
  @DisplayName("전체 유저 조회 - 성공")
  void getUserList_success() throws Exception {
    UserDto u1 = new UserDto(UUID.randomUUID(), "alice", "alice@codeit.com", null, true);
    UserDto u2 = new UserDto(UUID.randomUUID(), "bob", "bob@codeit.com", null, false);

    given(userService.findAll()).willReturn(List.of(u1, u2));

    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].username").value("alice"))
        .andExpect(jsonPath("$[1].username").value("bob"));
  }

  @Test
  @DisplayName("유저 삭제 - 성공")
  void deleteUser_success() throws Exception {
    UUID userId = UUID.randomUUID();
    willDoNothing().given(userService).delete(userId);

    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("유저 삭제 - 실패 (존재하지 않는 유저)")
  void deleteUser_fail_notFound() throws Exception {
    UUID userId = UUID.randomUUID();
    willThrow(new UserNotFoundException(userId))
        .given(userService).delete(userId);

    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("유저 상태 업데이트 - 성공")
  void updateUserStatus_success() throws Exception {
    UUID userId = UUID.randomUUID();
    Instant now = Instant.now();
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(now);
    UserStatusDto response = new UserStatusDto(UUID.randomUUID(), userId, now);

    given(userStatusService.updateByUserId(any(), any())).willReturn(response);

    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(userId.toString()));
  }

  @Test
  @DisplayName("유저 상태 업데이트 - 실패 (존재하지 않는 유저)")
  void updateUserStatus_fail_notFound() throws Exception {
    UUID userId = UUID.randomUUID();
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(Instant.now());

    willThrow(new UserNotFoundException(userId))
        .given(userStatusService).updateByUserId(any(), any());

    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }
}
