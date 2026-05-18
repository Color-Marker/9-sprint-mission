package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class RoleHierarchyConfig {

  @Bean
  public RoleHierarchy roleHierarchy() {

    RoleHierarchyImpl hierarchy =
        new RoleHierarchyImpl();

    // ROLE_ADMIN > ROLE_CHANNEL_MANAGER > ROLE_USER
    hierarchy.setHierarchy("""
        ROLE_ADMIN > ROLE_CHANNEL_MANAGER > ROLE_USER
        """);

    return hierarchy;
  }

  @Bean
  static MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      RoleHierarchy roleHierarchy) {
    DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy);
    return handler;
  }
}