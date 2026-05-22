package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.config.JwtTokenProvider;
import com.sprint.mission.discodeit.config.RefreshTokenStore;
import com.sprint.mission.discodeit.dto.data.JwtDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.DiscodeitUserDetails;
import com.sprint.mission.discodeit.registry.JwtInformation;
import com.sprint.mission.discodeit.registry.JwtRegistry;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.DiscodeitUserDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "CSRF Token API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtRegistry jwtRegistry;
  private final UserDetailsService userDetailsService;

  @GetMapping("csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    String tokenValue = csrfToken.getToken();
    log.debug("CSRF 토큰 요청: {}", tokenValue);
    return ResponseEntity.status(203).build();
  }

  @PutMapping("role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDto> updateRole(
      @RequestBody UserRoleUpdateRequest request
  ) {
    UserDto data = userService.updateRole(request);
    return ResponseEntity.ok(data);
  }

  @PostMapping("refresh")
  public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {

    String refreshToken = getRefreshTokenFromCookie(request);

    if (refreshToken == null) {
      return ResponseEntity.status(401).body("Refresh Token이 없습니다.");
    }

    if (!jwtRegistry.hasActiveJwtInformationByRefreshToken(refreshToken)) {
      return ResponseEntity.status(401).body("유효하지 않은 Refresh Token입니다.");
    }

    String username = jwtTokenProvider.getUsername(refreshToken);
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) userDetailsService.loadUserByUsername(
        username);
    UserDto userDto = userDetails.getUserDto();

    String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
    String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

    JwtInformation newJwtInformation = new JwtInformation(userDto, newAccessToken, newRefreshToken);
    jwtRegistry.rotateJwtInformation(refreshToken, newJwtInformation);

    Cookie refreshCookie = new Cookie("REFRESH_TOKEN", newRefreshToken);
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(false);
    refreshCookie.setPath("/api/auth");
    refreshCookie.setMaxAge(7 * 24 * 60 * 60);
    response.addCookie(refreshCookie);

    return ResponseEntity.ok(
        JwtDto.builder()
            .userDto(userDto)
            .accessToken(newAccessToken)
            .build()
    );
  }

  private String getRefreshTokenFromCookie(
      HttpServletRequest request
  ) {

    if (request.getCookies() == null) {
      return null;
    }

    return Arrays.stream(request.getCookies())
        .filter(cookie ->
            "REFRESH_TOKEN".equals(
                cookie.getName()
            )
        )
        .map(Cookie::getValue)
        .findFirst()
        .orElse(null);
  }

}
