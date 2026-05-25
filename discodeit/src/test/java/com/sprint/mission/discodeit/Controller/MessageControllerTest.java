package com.sprint.mission.discodeit.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.handler.GlobalExceptionHandler;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
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

@WebMvcTest(MessageController.class)
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
class MessageControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  MessageService messageService;

  @Test
  @DisplayName("메시지 생성 - 성공")
  void createMessage_success() throws Exception {
    UUID messageId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    Instant now = Instant.now();

    UserDto author = new UserDto(authorId, "guest", "guest@codeit.com", null, true);
    MessageDto response = new MessageDto(messageId, now, now, "hello", channelId, author,
        List.of());

    given(messageService.create(any(), any())).willReturn(response);

    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
        """
            {"content":"hello","channelId":"%s","authorId":"%s"}
            """.formatted(channelId, authorId).getBytes()
    );

    mockMvc.perform(multipart("/api/messages")
            .file(messagePart)
            .file(new MockMultipartFile("attachments", new byte[0])))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("hello"))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()));
  }

  @Test
  @DisplayName("메시지 생성 - 실패 (content 누락)")
  void createMessage_fail_validation() throws Exception {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MockMultipartFile messagePart = new MockMultipartFile(
        "messageCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
        """
            {"content":"","channelId":"%s","authorId":"%s"}
            """.formatted(channelId, authorId).getBytes()
    );

    mockMvc.perform(multipart("/api/messages").file(messagePart))
        .andDo(print())  // 여기 추가
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
  }

  @Test
  @DisplayName("메시지 수정 - 성공")
  void updateMessage_success() throws Exception {
    UUID messageId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    Instant now = Instant.now();
    UserDto author = new UserDto(UUID.randomUUID(), "guest", "guest@codeit.com", null, true);
    MessageDto response = new MessageDto(messageId, now, now, "수정된 메시지", channelId, author,
        List.of());

    given(messageService.update(any(), any())).willReturn(response);

    MessageUpdateRequest request = new MessageUpdateRequest("수정된 메시지");

    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("수정된 메시지"));
  }

  @Test
  @DisplayName("메시지 수정 - 실패 (존재하지 않는 메시지)")
  void updateMessage_fail_notFound() throws Exception {
    UUID messageId = UUID.randomUUID();

    willThrow(new MessageNotFoundException(messageId))
        .given(messageService).update(any(), any());

    MessageUpdateRequest request = new MessageUpdateRequest("수정된 메시지");

    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("메시지 삭제 - 성공")
  void deleteMessage_success() throws Exception {
    UUID messageId = UUID.randomUUID();
    willDoNothing().given(messageService).delete(messageId);

    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("메시지 삭제 - 실패 (존재하지 않는 메시지)")
  void deleteMessage_fail_notFound() throws Exception {
    UUID messageId = UUID.randomUUID();

    willThrow(new MessageNotFoundException(messageId))
        .given(messageService).delete(messageId);

    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("채널 별 메시지 조회 - 성공 (커서 없음)")
  void getMessageList_success() throws Exception {
    UUID channelId = UUID.randomUUID();
    Instant now = Instant.now();
    UserDto author = new UserDto(UUID.randomUUID(), "guest", "guest@codeit.com", null, true);
    MessageDto msg = new MessageDto(UUID.randomUUID(), now, now, "hello", channelId, author,
        List.of());

    PageResponse<MessageDto> pageResponse = new PageResponse<>(List.of(msg), now.minusSeconds(1),
        50, false, 1L);

    given(messageService.findAllByChannelId(any(), any(), any())).willReturn(pageResponse);

    mockMvc.perform(get("/api/messages").param("channelId", channelId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].content").value("hello"))
        .andExpect(jsonPath("$.hasNext").value(false));
  }

  @Test
  @DisplayName("채널 별 메시지 조회 - 실패 (channelId 누락)")
  void getMessageList_fail_missingChannelId() throws Exception {
    mockMvc.perform(get("/api/messages"))
        .andExpect(status().isBadRequest());
  }
}