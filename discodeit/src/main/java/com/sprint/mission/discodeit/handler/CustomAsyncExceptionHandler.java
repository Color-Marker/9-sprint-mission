package com.sprint.mission.discodeit.handler;

import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

  @Override
  public void handleUncaughtException(Throwable ex, Method method, Object... params) {
    log.warn("[GlobalAsyncExceptionHandler] 예외 발생");
    log.warn("[GlobalAsyncExceptionHandler] 메서드:" + method.getName());
    log.warn("[GlobalAsyncExceptionHandler] 파라미터:" + Arrays.toString(params));
    log.warn("[GlobalAsyncExceptionHandler] 예외 타입:" + ex.getClass().getSimpleName());
    log.warn("[GlobalAsyncExceptionHandler] 예외 메시지:" + ex.getMessage());
  }
}
