package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.handler.LoginSuccessHandler;
import com.sprint.mission.discodeit.handler.SpaCsrfTokenRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, LoginSuccessHandler loginSuccessHandler,
      LoginFailureHandler loginFailureHandler)
      throws Exception {
    http
        .authorizeHttpRequests(authz -> authz
            // 프론트 코드 및 기본 페이지 허용
            .requestMatchers("/", "/index.html", "/assets/**").permitAll()
            .requestMatchers("/api/auth/role").hasRole("ADMIN")
            .requestMatchers("/api/auth/**").permitAll()
            // 회원 가입 요청 허용
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            // Swagger
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            // Actuator
            .requestMatchers("/actuator/**").permitAll()
            .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
        )
        //.formLogin(Customizer.withDefaults());
        .formLogin(login -> login
            .loginProcessingUrl("/api/auth/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
        )
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(
                new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
        )
        .exceptionHandling(ex -> ex
            // 로그인 안 한 경우
            .authenticationEntryPoint((request, response, authException) -> {
              response.setStatus(HttpStatus.UNAUTHORIZED.value());
              response.setContentType("application/json");
              response.getWriter().write("{\"message\": \"인증이 필요합니다.\"}");
            })
            // 로그인은 했다 쳐도 권한이 안 되는 경우
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              response.setStatus(HttpStatus.FORBIDDEN.value());
              response.setContentType("application/json");
              response.getWriter().write("{\"message\": \"접근 권한이 없습니다.\"}");
            })
        );

    return http.build();
  }
}