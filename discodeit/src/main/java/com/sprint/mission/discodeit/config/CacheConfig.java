package com.sprint.mission.discodeit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import java.time.Duration;
import java.time.LocalTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

  @Bean
  public CacheManager cacheManager() {

    CaffeineCacheManager cacheManager =
        new CaffeineCacheManager();

    cacheManager.registerCustomCache(
        "UserList",
        Caffeine.newBuilder()
            .maximumSize(100)
            .recordStats()
            .removalListener((Object key, Object value, RemovalCause cause) -> {
              log.info("[{}] [CacheUserList 무효화] key={}, value={}, cause={}",
                  LocalTime.now().withNano(0), key, value, cause);
            })
            .build()
    );
    cacheManager.registerCustomCache(
        "ChannelList",
        Caffeine.newBuilder()
            .maximumSize(100)
            .recordStats()
            .removalListener((Object key, Object value, RemovalCause cause) -> {
              log.info("[{}] [CacheChannelList 무효화] key={}, value={}, cause={}",
                  LocalTime.now().withNano(0), key, value, cause);
            })
            .build()
    );
    cacheManager.registerCustomCache(
        "NotificationList",
        Caffeine.newBuilder()
            .maximumSize(100)
            .recordStats()
            .removalListener((Object key, Object value, RemovalCause cause) -> {
              log.info("[{}] [CacheNotificationList 무효화] key={}, value={}, cause={}",
                  LocalTime.now().withNano(0), key, value, cause);
            })
            .build()
    );

    return cacheManager;
  }
}
