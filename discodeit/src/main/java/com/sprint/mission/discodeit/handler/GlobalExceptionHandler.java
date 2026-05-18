package com.sprint.mission.discodeit.handler;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import com.sprint.mission.discodeit.exception.binaryContent.FileNotFoundException;
import com.sprint.mission.discodeit.exception.binaryContent.FilePathException;
import com.sprint.mission.discodeit.exception.binaryContent.FileReadException;
import com.sprint.mission.discodeit.exception.binaryContent.FileWriteException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateNameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusAlreadyExistException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException e) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.CONFLICT.value()
        ));
  }

  @ExceptionHandler(DuplicateNameException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateNameException(DuplicateNameException e) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.CONFLICT.value()
        ));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.NOT_FOUND.value()
        ));
  }

  @ExceptionHandler(UserStatusAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> userStatusAlreadyExistException(
      UserStatusAlreadyExistException e) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.CONFLICT.value()
        ));
  }


  @ExceptionHandler(UserStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> userStatusNotFoundException(UserStatusNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.NOT_FOUND.value()
        ));
  }

  @ExceptionHandler(ReadStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> readStatusNotFoundException(ReadStatusNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.NOT_FOUND.value()
        ));
  }

  @ExceptionHandler(ReadStatusAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> readStatusAlreadyExistException(
      ReadStatusAlreadyExistException e) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.CONFLICT.value()
        ));
  }

  @ExceptionHandler(MessageNotFoundException.class)
  public ResponseEntity<ErrorResponse> messageNotFoundException(MessageNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.NOT_FOUND.value()
        ));
  }

  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<ErrorResponse> channelNotFoundException(ChannelNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.NOT_FOUND.value()
        ));
  }

  @ExceptionHandler(PrivateChannelUpdateException.class)
  public ResponseEntity<ErrorResponse> privateChannelUpdateException(
      PrivateChannelUpdateException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        ));
  }

  @ExceptionHandler(FileNotFoundException.class)
  public ResponseEntity<ErrorResponse> fileNotFoundException(FileNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.NOT_FOUND.value()
        ));
  }

  @ExceptionHandler(FilePathException.class)
  public ResponseEntity<ErrorResponse> filePathException(FilePathException e) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.CONFLICT.value()
        ));
  }

  @ExceptionHandler(FileReadException.class)
  public ResponseEntity<ErrorResponse> fileReadException(FileReadException e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        ));
  }

  @ExceptionHandler(FileWriteException.class)
  public ResponseEntity<ErrorResponse> fileWriteException(FileWriteException e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .body(new ErrorResponse(
            e.getTimestamp(),
            e.getErrorCode().toString(),
            e.getMessage(),
            e.getDetails(),
            e.getClass().getSimpleName(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException e) {
    Map<String, Object> details = new HashMap<>();
    e.getBindingResult().getFieldErrors()
        .forEach(error ->
            details.put(error.getField(), error.getDefaultMessage()));
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST.value())
        .body(new ErrorResponse(
            Instant.now(),
            "VALIDATION_FAILED",
            "입력값이 유효하지 않습니다.",
            details,
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        ));
  }
}


