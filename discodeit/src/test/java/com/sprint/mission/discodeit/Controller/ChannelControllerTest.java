package com.sprint.mission.discodeit.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.GlobalExceptionHandler;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
@Import(GlobalExceptionHandler.class)
class ChannelControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;
  @MockitoBean
  ChannelService channelService;

  @Test
  @DisplayName("public 채널 생성 - 성공")
  void createPublicChannel_success() throws Exception {
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.builder().name("general").type(ChannelType.PUBLIC).build();
    ChannelDto response = new ChannelDto(channelId, ChannelType.PUBLIC, "general", "공지 채널",
        List.of(), Instant.now());

    given(channelService.create(any(PublicChannelCreateRequest.class))).willReturn(channel);
    given(channelService.find(any())).willReturn(response);

    PublicChannelCreateRequest request = new PublicChannelCreateRequest("general", "공지 채널");

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("general"))
        .andExpect(jsonPath("$.type").value("PUBLIC"));
  }

  @Test
  @DisplayName("public 채널 생성 - 실패 (validation: name 누락)")
  void createPublicChannel_fail_validation() throws Exception {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("", "설명");

    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
  }

  @Test
  @DisplayName("private 채널 생성 - 성공")
  void createPrivateChannel_success() throws Exception {
    UUID channelId = UUID.randomUUID();
    List<UUID> participants = List.of(UUID.randomUUID(), UUID.randomUUID());
    Channel channel = Channel.builder().name("dm").type(ChannelType.PRIVATE).build();
    ChannelDto response = new ChannelDto(channelId, ChannelType.PRIVATE, "dm", null, List.of(),
        null);

    given(channelService.create(any(PrivateChannelCreateRequest.class))).willReturn(channel);
    given(channelService.find(any())).willReturn(response);

    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participants, "dm", null);

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.type").value("PRIVATE"));
  }

  @Test
  @DisplayName("private 채널 생성 - 실패 (validation: participantIds 누락)")
  void createPrivateChannel_fail_validation() throws Exception {
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(null, "dm", null);

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
  }

  @Test
  @DisplayName("public 채널 수정 - 성공")
  void updatePublicChannel_success() throws Exception {
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.builder().name("updated").type(ChannelType.PUBLIC).build();
    ChannelDto response = new ChannelDto(channelId, ChannelType.PUBLIC, "updated", "수정된 설명",
        List.of(), null);

    given(channelService.update(any(), any())).willReturn(channel);
    given(channelService.find(channelId)).willReturn(response);

    PublicChannelCreateRequest request = new PublicChannelCreateRequest("updated", "수정된 설명");

    mockMvc.perform(put("/api/channels/public/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("updated"));
  }

  @Test
  @DisplayName("public 채널 수정 - 실패 (private 채널은 수정 불가)")
  void updatePublicChannel_fail_privateChannel() throws Exception {
    UUID channelId = UUID.randomUUID();

    willThrow(new PrivateChannelUpdateException(channelId))
        .given(channelService).update(any(), any());

    PublicChannelCreateRequest request = new PublicChannelCreateRequest("updated", "수정된 설명");

    mockMvc.perform(put("/api/channels/public/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("채널 삭제 - 성공")
  void deleteChannel_success() throws Exception {
    UUID channelId = UUID.randomUUID();
    willDoNothing().given(channelService).delete(channelId);

    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("채널 삭제 - 실패 (존재하지 않는 채널)")
  void deleteChannel_fail_notFound() throws Exception {
    UUID channelId = UUID.randomUUID();
    willThrow(new ChannelNotFoundException(channelId))
        .given(channelService).delete(channelId);

    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("유저 소속 채널 조회 - 성공")
  void getChannelList_success() throws Exception {
    UUID userId = UUID.randomUUID();
    ChannelDto c1 = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "general", "설명",
        List.of(), null);
    ChannelDto c2 = new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, "dm", null, List.of(),
        null);

    given(channelService.findAllByUserId(userId)).willReturn(List.of(c1, c2));

    mockMvc.perform(get("/api/channels").param("userId", userId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("general"));
  }
}