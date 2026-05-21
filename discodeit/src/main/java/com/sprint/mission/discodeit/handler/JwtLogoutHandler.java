package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.config.RefreshTokenStore;
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

  private final RefreshTokenStore refreshTokenStore;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    String refreshToken = null;
    if (request.getCookies() != null) {
      refreshToken = Arrays.stream(request.getCookies())
          .filter(c -> "REFRESH_TOKEN".equals(c.getName()))
          .map(Cookie::getValue)
          .findFirst()
          .orElse(null);
    }

    if (refreshToken != null) {
      refreshTokenStore.remove(refreshToken);
    }

    Cookie expiredCookie = new Cookie("REFRESH_TOKEN", null);
    expiredCookie.setHttpOnly(true);
    expiredCookie.setPath("/api/auth");
    expiredCookie.setMaxAge(0);  // 즉시 만료
    response.addCookie(expiredCookie);
  }
}
