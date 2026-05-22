package com.sprint.mission.discodeit.registry;

import java.util.UUID;

public class InMemoryJwtRegistry implements JwtRegistry {

  @Override
  public void registerJwtInformation(JwtInformation jwtInformation) {

  }

  @Override
  public void invalidateJwtInformationByUserId(UUID userId) {

  }

  @Override
  public void hasActiveJwtInformationByUserId(UUID userId) {

  }

  @Override
  public void hasActiveJwtInformationByAccessToken(String accessToken) {

  }

  @Override
  public void hasActiveJwtInformationByRefreshToken(String refreshToken) {

  }

  @Override
  public void rotateJwtInformation(String refreshToken, String newJwtInformation) {

  }

  @Override
  public void clearExpiredJwtInformation() {

  }
}
