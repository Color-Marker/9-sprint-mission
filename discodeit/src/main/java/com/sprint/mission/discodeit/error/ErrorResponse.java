package com.sprint.mission.discodeit.error;

import jakarta.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ErrorResponse {

  private List<FieldError> fieldErrors;
  private List<ConstraintViolationError> violationErrors;

  public ErrorResponse(List<FieldError> fieldErrors,
      List<ConstraintViolationError> violationErrors) {
    this.fieldErrors = fieldErrors;
    this.violationErrors = violationErrors;
  }

  public static ErrorResponse of(BindingResult bindingResult) {
    return new ErrorResponse(FieldError.of(bindingResult), null);
  }

  public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
    return new ErrorResponse(null, ConstraintViolationError.of(violations));
  }

  @Getter
  public static class FieldError {

    private String field;
    private Object rejectedValue;
    private String reason;

    public FieldError(String field, Object rejectedValue, String reason) {
      this.field = field;
      this.rejectedValue = rejectedValue;
      this.reason = reason;
    }

    public static List<FieldError> of(BindingResult bindingResult) {
      return bindingResult.getFieldErrors().stream()
          .map(error -> new FieldError(
              error.getField(),
              error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
              error.getDefaultMessage()))
          .collect(Collectors.toList());
    }
  }

  @Getter
  public static class ConstraintViolationError {

    private String propertyPath;
    private Object rejectedValue;
    private String reason;

    private ConstraintViolationError(String propertyPath, Object rejectedValue, String reason) {
      this.propertyPath = propertyPath;
      this.rejectedValue = rejectedValue;
      this.reason = reason;
    }

    public static List<ConstraintViolationError> of(
        Set<ConstraintViolation<?>> constraintViolations) {
      return constraintViolations.stream()
          .map(cv -> new ConstraintViolationError(
              cv.getPropertyPath().toString(),
              cv.getInvalidValue(),
              cv.getMessage()))
          .collect(Collectors.toList());
    }
  }
}
