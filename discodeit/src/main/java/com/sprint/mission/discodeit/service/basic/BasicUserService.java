package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.DiscodeitUserDetails;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateNameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final PasswordEncoder passwordEncoder;
  private final SessionRegistry sessionRegistry;

  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (userRepository.existsByEmail(email)) {
      log.warn("유저 생성 실패 - 이메일 {} 은 이미 존재합니다.", email);
      throw new DuplicateEmailException(email);
    }
    if (userRepository.existsByUsername(username)) {
      log.warn("유저 생성 실패 - 이름 {} 은 이미 존재합니다.", username);
      throw new DuplicateNameException(username);
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          BinaryContent content = binaryContentRepository.save(binaryContent);
          log.debug("프로필 사진 저장 - 파일 상세: {}", content);
          binaryContentStorage.put(content.getId(), bytes);
          return content;
        })
        .orElse(null);
    String password = userCreateRequest.password();

    User user = new User(
        username,
        email,
        passwordEncoder.encode(password),
        nullableProfile,
        Role.USER
    );
    Instant now = Instant.now();
    User newUser = userRepository.save(user);
    log.info("유저 생성 및 저장 완료 - 유저: {}", newUser);

    return userMapper.toDto(user);
  }

  @Transactional(readOnly = true)
  @Override
  public UserDto find(UUID userId) {
    return userRepository.findWithProfileAndStatusById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> new UserNotFoundException(userId));
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserDto> findAll() {
    return userRepository.findAllWithProfileAndStatusBy()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @PreAuthorize("#userId == authentication.principal.userDto.id or hasRole('ADMIN')")
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("유저 검색 실패 - 유저 ID: {}", userId);
          return new UserNotFoundException(userId);
        });

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (newEmail != null && !newEmail.equals(user.getEmail())) {
      if (userRepository.existsByEmail(newEmail)) {
        log.warn("유저 업데이트 실패 - 중복된 이메일: {}", newEmail);
        throw new DuplicateEmailException(newEmail);
      }
    }
    if (newUsername != null & !newUsername.equals(user.getUsername())) {
      if (userRepository.existsByUsername(newUsername)) {
        log.warn("유저 업데이트 실패 - 중복된 이름: {}", newUsername);
        throw new DuplicateNameException(newUsername);
      }
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          BinaryContent content = binaryContentRepository.save(binaryContent);
          log.debug("프로필 사진 저장 - 파일 상세: {}", content);
          binaryContentStorage.put(content.getId(), bytes);
          return content;
        })
        .orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    log.debug("유저 업데이트 실행 - 유저: {}", user);
    user.update(newUsername, newEmail, newPassword, nullableProfile);
    log.info("유저 업데이트 완료 - 유저: {}", user);
    return userMapper.toDto(user);
  }

  @PreAuthorize("#userId == authentication.principal.userDto.id or hasRole('ADMIN')")
  @Override
  public void delete(UUID userId) {
    if (!userRepository.existsById(userId)) {
      log.warn("유저 검색 실패 - 유저 ID: {}", userId);
      throw new UserNotFoundException(userId);
    }

    log.info("유저 삭제 진행 - 유저 ID: {}", userId);
    userRepository.deleteById(userId);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public UserDto updateRole(UserRoleUpdateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new UserNotFoundException(request.userId()));
    user.updateRole(request.newRole());
    // 세션 무효화
    sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> principal instanceof DiscodeitUserDetails)
        .map(principal -> (DiscodeitUserDetails) principal)
        .filter(details -> details.getUsername().equals(user.getUsername()))
        .flatMap(details -> sessionRegistry.getAllSessions(details, false).stream())
        .forEach(SessionInformation::expireNow);

    return userMapper.toDto(user);
  }
}