package com.sprint.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;


import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateNameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @InjectMocks
  private BasicUserService userService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStatusRepository userStatusRepository;
  @Mock
  private UserMapper userMapper;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Test
  @DisplayName("유저 생성 성공 테스트")
  void createSuccessTest() {
    UUID userId = UUID.randomUUID();
    UUID contentId = UUID.randomUUID();
    UserCreateRequest request = new UserCreateRequest("test", "test@test.com", "test");
    BinaryContentCreateRequest fileRequest = new BinaryContentCreateRequest("test.png",
        "test".getBytes(), "image/png");
    Optional<BinaryContentCreateRequest> profileRequest = Optional.of(fileRequest);

    BinaryContent savedFile = BinaryContent.builder()
        .id(contentId)
        .fileName("test.png")
        .size((long) "test".getBytes().length)
        .contentType("image/png")
        .build();

    User savedUser = User.builder()
        .id(userId)
        .username("test")
        .email("test@test.com")
        .password("test")
        .profile(savedFile)
        .build();

    BinaryContentDto binaryContentDto = new BinaryContentDto(contentId, "test.png",
        (long) "test".getBytes().length, "image/png");
    UserDto expectedDto = new UserDto(userId, "test", "test@test.com", binaryContentDto, true);

    given(userRepository.existsByEmail(anyString())).willReturn(false);
    given(userRepository.existsByUsername(anyString())).willReturn(false);
    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(savedFile);
    given(userRepository.save(any(User.class))).willReturn(savedUser);
    given(userMapper.toDto(any(User.class))).willReturn(expectedDto);

    UserDto response = userService.create(request, profileRequest);

    assertEquals(expectedDto, response);

    then(userRepository).should(times(1)).save(any(User.class));
    then(userStatusRepository).should(times(1)).save(any(UserStatus.class));
    then(binaryContentStorage).should().put(eq(contentId), any(byte[].class));
  }

  @Test
  @DisplayName("유저 생성 실패 테스트 - 중복 이메일 가입 시도")
  void createDupEmailFailTest() {
    UserCreateRequest request = new UserCreateRequest("test", "email@test.com", "test");
    Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
    given(userRepository.existsByEmail(request.email())).willReturn(true);

    assertThrows(DuplicateEmailException.class, () -> {
      userService.create(request, profileRequest);
    });

    then(userRepository).should(never()).save(any(User.class));
    then(binaryContentRepository).shouldHaveNoInteractions();
    then(binaryContentStorage).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("유저 생성 실패 테스트 - 중복 이름 가입 시도")
  void createDupNameFailTest() {
    UserCreateRequest request = new UserCreateRequest("name", "test@test.com", "test");
    Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();

    given(userRepository.existsByUsername(request.username())).willReturn(true);

    assertThrows(DuplicateNameException.class, () -> {
      userService.create(request, profileRequest);
    });

    then(userRepository).should(never()).save(any(User.class));
    then(binaryContentRepository).shouldHaveNoInteractions();
    then(binaryContentStorage).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("유저 수정 성공 테스트")
  void updateSuccessTest() {
    UUID userId = UUID.randomUUID();
    UUID contentId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("test", "test@test.com", "test");
    BinaryContentCreateRequest fileRequest = new BinaryContentCreateRequest("test.png",
        "test".getBytes(), "image/png");
    Optional<BinaryContentCreateRequest> profileRequest = Optional.of(fileRequest);

    BinaryContent savedFile = BinaryContent.builder()
        .id(contentId)
        .fileName("test.png")
        .size((long) "test".getBytes().length)
        .contentType("image/png")
        .build();

    User beforeUser = User.builder()
        .id(userId)
        .username("tt")
        .email("ttt@test.com")
        .password("ttt")
        .profile(savedFile)
        .build();

    User afterUser = User.builder()
        .id(userId)
        .username("test")
        .email("test@test.com")
        .password("test")
        .profile(savedFile)
        .build();

    BinaryContentDto binaryContentDto = new BinaryContentDto(contentId, "test.png",
        (long) "test".getBytes().length, "image/png");
    UserDto expectedDto = new UserDto(userId, "test", "test@test.com", binaryContentDto, true);

    given(userRepository.findById(userId)).willReturn(Optional.ofNullable(beforeUser));
    given(userRepository.existsByEmail(anyString())).willReturn(false);
    given(userRepository.existsByUsername(anyString())).willReturn(false);
    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(savedFile);
    given(userMapper.toDto(any(User.class))).willReturn(expectedDto);

    UserDto response = userService.update(userId, request, profileRequest);

    assertEquals(expectedDto, response);

    then(binaryContentStorage).should().put(eq(contentId), any(byte[].class));
  }

  @Test
  @DisplayName("유저 수정 실패 테스트 - 유저 검색 실패")
  void updateFindFailTest() {
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("test", "test@test.com", "test");
    Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();

    given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      userService.update(userId, request, profileRequest);
    });
    then(binaryContentRepository).shouldHaveNoInteractions();
    then(binaryContentStorage).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("유저 수정 실패 테스트 - 중복 이메일")
  void updateDupEmailFailTest() {
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("test", "test@test.com", "test");
    Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
    User user = User.builder()
        .id(userId)
        .username("oldtest")
        .email("oldtest@test.com")
        .password("oldtest")
        .profile(null)
        .build();
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userRepository.existsByEmail(request.newEmail())).willReturn(true);

    assertThrows(DuplicateEmailException.class, () -> {
      userService.update(userId, request, profileRequest);
    });
    then(userRepository).should().existsByEmail(request.newEmail());
    then(binaryContentRepository).shouldHaveNoInteractions();
    then(binaryContentStorage).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("유저 수정 실패 테스트 - 죽복 이름")
  void updateDupNameFailTest() {
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("test", "test@test.com", "test");
    Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
    User user = User.builder()
        .id(userId)
        .username("oldtest")
        .email("oldtest@test.com")
        .password("oldtest")
        .profile(null)
        .build();
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userRepository.existsByUsername(request.newUsername())).willReturn(true);

    assertThrows(DuplicateNameException.class, () -> {
      userService.update(userId, request, profileRequest);
    });
    then(userRepository).should().existsByUsername(request.newUsername());
    then(binaryContentRepository).shouldHaveNoInteractions();
    then(binaryContentStorage).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("유저 삭제 성공 테스트")
  void deleteSuccessTest() {
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(true);
    userService.delete(userId);
    then(userRepository).should().existsById(userId);
    then(userRepository).should().deleteById(userId);
  }

  @Test
  @DisplayName("유저 삭제 실패 테스트 - 검색 실패")
  void deleteFailTest() {
    UUID userId = UUID.randomUUID();

    given(userRepository.existsById(userId)).willReturn(false);

    assertThrows(UserNotFoundException.class, () -> {
      userService.delete(userId);
    });
    then(userRepository).should().existsById(userId);
    then(userRepository).should(never()).deleteById(any(UUID.class));
  }
}
