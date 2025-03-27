package com.ss.prodsel.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrorResponse handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
    //List<String> errors = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
    return new ApiErrorResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(), "Invalid product ID", List.of());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrorResponse handleInvalidRequest(MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<String> errors = new ArrayList<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    return new ApiErrorResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(), "Validation failed", errors);
  }

  @ExceptionHandler(ProductNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiErrorResponse handleMissingProduct(ProductNotFoundException ex, HttpServletRequest request) {
    return new ApiErrorResponse(HttpStatus.NOT_FOUND, request.getRequestURI(), ex.getMessage(), List.of());
  }

  @ExceptionHandler(DuplicateProductException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrorResponse handleDuplicateProduct(DuplicateProductException ex, HttpServletRequest request) {
    return new ApiErrorResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(), ex.getMessage(), List.of());
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiErrorResponse handleUnauthorized(AuthorizationDeniedException ex, HttpServletRequest request) {
    return new ApiErrorResponse(HttpStatus.FORBIDDEN, request.getRequestURI(), "Insufficient Permission",
      List.of(ex.getMessage()));
  }

}
