package com.sprint.mission.discodeit.registry;

import java.util.UUID;

public interface JwtRegistry {

  void registerJwtInformation(JwtInformation jwtInformation);

  void invalidateJwtInformationByUserId(UUID userId);

  void hasActiveJwtInformationByUserId(UUID userId);

  void hasActiveJwtInformationByAccessToken(String accessToken);

  void hasActiveJwtInformationByRefreshToken(String refreshToken);

  void rotateJwtInformation(String refreshToken, String newJwtInformation);

  void clearExpiredJwtInformation();
}
