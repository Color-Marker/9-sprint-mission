package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
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
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

//  카페인 사용 시 사용
//  @Bean
//  public CacheManager cacheManager() {
//
//    CaffeineCacheManager cacheManager =
//        new CaffeineCacheManager();
//
//    cacheManager.registerCustomCache(
//        "UserList",
//        Caffeine.newBuilder()
//            .maximumSize(100)
//            .recordStats()
//            .removalListener((Object key, Object value, RemovalCause cause) -> {
//              log.info("[{}] [CacheUserList 무효화] key={}, value={}, cause={}",
//                  LocalTime.now().withNano(0), key, value, cause);
//            })
//            .build()
//    );
//    cacheManager.registerCustomCache(
//        "ChannelList",
//        Caffeine.newBuilder()
//            .maximumSize(100)
//            .recordStats()
//            .removalListener((Object key, Object value, RemovalCause cause) -> {
//              log.info("[{}] [CacheChannelList 무효화] key={}, value={}, cause={}",
//                  LocalTime.now().withNano(0), key, value, cause);
//            })
//            .build()
//    );
//    cacheManager.registerCustomCache(
//        "NotificationList",
//        Caffeine.newBuilder()
//            .maximumSize(100)
//            .recordStats()
//            .removalListener((Object key, Object value, RemovalCause cause) -> {
//              log.info("[{}] [CacheNotificationList 무효화] key={}, value={}, cause={}",
//                  LocalTime.now().withNano(0), key, value, cause);
//            })
//            .build()
//    );
//
//    return cacheManager;
//  }

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory connectionFactory,
      RedisCacheConfiguration redisCacheConfiguration) {
    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(redisCacheConfiguration)
        .withCacheConfiguration("UserList", redisCacheConfiguration)
        .withCacheConfiguration("ChannelList", redisCacheConfiguration)
        .withCacheConfiguration("NotificationList", redisCacheConfiguration)
        .build();
  }

  // CacheConfig
  @Bean
  public RedisCacheConfiguration redisCacheConfiguration(ObjectMapper objectMapper) {
    ObjectMapper redisObjectMapper = objectMapper.copy();
    redisObjectMapper.activateDefaultTyping(
        LaissezFaireSubTypeValidator.instance,
        DefaultTyping.EVERYTHING,
        As.PROPERTY
    );

    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer(redisObjectMapper)
            )
        )
        .prefixCacheNameWith("discodeit:")
        .entryTtl(Duration.ofSeconds(600))
        .disableCachingNullValues();
  }

}
