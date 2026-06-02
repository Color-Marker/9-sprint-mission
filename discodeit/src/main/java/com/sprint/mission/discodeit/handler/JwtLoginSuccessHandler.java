package com.sprint.mission.discodeit.handler;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.sprint.mission.discodeit.config.JwtTokenProvider;
import com.sprint.mission.discodeit.config.RefreshTokenStore;
import com.sprint.mission.discodeit.dto.data.JwtDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.DiscodeitUserDetails;
import com.sprint.mission.discodeit.registry.JwtInformation;
import com.sprint.mission.discodeit.registry.JwtRegistry;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtLoginSuccessHandler
    implements AuthenticationSuccessHandler {

  private final JwtTokenProvider
      jwtTokenProvider;

  private final ObjectMapper
      objectMapper;
  private final JwtRegistry jwtRegistry;

  @CacheEvict(value = "UserList", key = "'all_users'")
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
    UserDto userDto = userDetails.getUserDto();

    String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
    String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

    // JwtRegistry에 등록 (동시 로그인 제한도 여기서 자동 처리)
    JwtInformation jwtInformation = new JwtInformation(userDto, accessToken, refreshToken);
    jwtRegistry.registerJwtInformation(jwtInformation);

    jakarta.servlet.http.Cookie refreshCookie = new jakarta.servlet.http.Cookie("REFRESH_TOKEN",
        refreshToken);
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(false);
    refreshCookie.setPath("/api/auth");
    refreshCookie.setMaxAge(7 * 24 * 60 * 60);
    response.addCookie(refreshCookie);

    JwtDto responseBody = JwtDto.builder()
        .userDto(userDto)
        .accessToken(accessToken)
        .build();

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getWriter(), responseBody);

    log.info("JWT login success: {}", userDetails.getUsername());
  }
}