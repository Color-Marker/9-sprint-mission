package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.registry.JwtRegistry;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

  private final JwtRegistry jwtRegistry;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {

    if (request.getCookies() == null) {
      return;
    }

    Arrays.stream(request.getCookies())
        .filter(cookie -> cookie.getName().equals("REFRESH_TOKEN"))
        .findFirst()
        .ifPresent(cookie -> {
          jwtRegistry.findUserIdByRefreshToken(cookie.getValue())
              .ifPresent(jwtRegistry::invalidateJwtInformationByUserId);

          Cookie expiredCookie = new Cookie("REFRESH_TOKEN", null);
          expiredCookie.setHttpOnly(true);
          expiredCookie.setPath("/api/auth");
          expiredCookie.setMaxAge(0);
          response.addCookie(expiredCookie);
        });
  }
}