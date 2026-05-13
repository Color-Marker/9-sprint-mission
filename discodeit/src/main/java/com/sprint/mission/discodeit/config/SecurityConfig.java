package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.handler.LoginSuccessHandler;
import com.sprint.mission.discodeit.handler.SpaCsrfTokenRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, LoginSuccessHandler loginSuccessHandler,
      LoginFailureHandler loginFailureHandler)
      throws Exception {
    http
        .authorizeHttpRequests(authz -> authz
            // 프론트 코드 및 기본 페이지 허용
            .requestMatchers("/", "/index.html", "/assets/**").permitAll()
            // 기본 페이지 접근 시 /api/auth/me로 요청 보냄 -> 여기서 401로 쿠키 없으면 쳐내서
            // 프론트 딴에서 로그인 페이지 작동되게 함.
            .requestMatchers("/api/auth/me").permitAll()
            // 회원 가입 요청 허용
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
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
        );

    return http.build();
  }
}
