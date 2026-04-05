package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

public class MDCLoggingInterceptor implements HandlerInterceptor {

  private static final String REQUEST_ID_HEADER = "Discodeit-Request-ID";

  private static final String MDC_REQUEST_ID = "requestId";
  private static final String MDC_REQUEST_URL = "requestUrl";
  private static final String MDC_HTTP_METHOD = "httpMethod";

  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response,
      Object handler) {

    String requestId = UUID.randomUUID().toString();

    MDC.put(MDC_REQUEST_ID, requestId);
    MDC.put(MDC_REQUEST_URL, request.getRequestURI());
    MDC.put(MDC_HTTP_METHOD, request.getMethod());

    response.setHeader(REQUEST_ID_HEADER, requestId);

    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      Exception ex) {
    MDC.clear();
  }
}